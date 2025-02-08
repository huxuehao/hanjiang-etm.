package com.hanjiang.etm.resource.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TAttach;
import com.hanjiang.etm.common.entity.TAttachChunk;
import com.hanjiang.etm.common.entity.TAttachLog;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.util.WebUtil;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.resource.AttachOptType;
import com.hanjiang.etm.resource.mapper.TAttachChunkMapper;
import com.hanjiang.etm.resource.mapper.TAttachMapper;
import com.hanjiang.etm.resource.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述：附件表
 *
 * @author huxuehao
 **/
@Service
public class TAttachServiceImpl extends ServiceImpl<TAttachMapper, TAttach> implements TAttachService {
    private final TStorageProtocolService storageProtocolService;
    private final TAttachLogService attachLogService;
    private final ParamsAdapter paramsAdapter;
    private final TAttachChunkMapper attachChunkMapper;

    public TAttachServiceImpl(TStorageProtocolService storageProtocolService, TAttachLogService attachLogService, ParamsAdapter paramsAdapter, TAttachChunkMapper attachChunkMapper) {
        this.storageProtocolService = storageProtocolService;
        this.attachLogService = attachLogService;
        this.paramsAdapter = paramsAdapter;
        this.attachChunkMapper = attachChunkMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteV2(List<Long> ids) {
        List<TAttach> tAttaches = listByIds(ids);
        for (TAttach attach : tAttaches) {
            removeById(attach.getId());

            /*
             * TODO 获取文件存储服务，并删除真实存储的文件，请结合实际，自行决定是否删除真实存储的文件
             */
            FileStorageService storageService = storageProtocolService.getStorageService();
            storageService.delete(genStoragePath(attach));

            attachHandelLog(attach, AttachOptType.DELETE);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TAttach upload(MultipartFile multipartFile, String originalFilename) {
        String extension = FileNameUtil.getSuffix(originalFilename);
        long size = multipartFile.getSize();

        // 文件类型验证
        String allowUploadFileType = paramsAdapter.getValue("ALLOW_UPLOAD_FILE_TYPE");
        if (extension == null || !allowUploadFileType.contains(extension)) {
            throw new RuntimeException("["+extension+"]文件类型不被接受");
        }

        // 文件大小验证
        double bm = bytesToMB(size);
        String maxSize = paramsAdapter.getValue("SINGLE_FILE_MAX_SIZE");
        if (bm > Double.parseDouble(maxSize)) {
            throw new RuntimeException("单个文件大小不可超过" + maxSize + "MB");
        }

        // 获取文件存储服务
        FileStorageService storageService = storageProtocolService.getStorageService();

        // 保存附件信息
        String storePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        TAttach attach = saveAttachInfo(originalFilename, extension, size, storageService.getProtocol(), storePath);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 保存文件
            storageService.save(inputStream, genStoragePath(attach));
            // 日志记录
            attachHandelLog(attach, AttachOptType.UPLOAD);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }
        return attach;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TAttach uploadChunkAndMerge(MultipartFile multipartFile,
                               String hash,
                               int totalSize,
                               int index,
                               int totalChunks,
                               String key,
                               String fileName) {
        // 成立条件：只有一个文件分片
        if (totalChunks == 1) {
            // 执行单文件上传
            return upload(multipartFile, fileName);
        }


        // 判断分片是否存在
        if(currentChunkExist(hash, index, key) && totalSize > (index+1)) {
            return new TAttach();
        }


        // 获取后缀
        String extension = FileNameUtil.getSuffix(fileName);
        // 文件类型验证
        String allowUploadFileType = paramsAdapter.getValue("ALLOW_UPLOAD_FILE_TYPE");
        if (extension == null || !allowUploadFileType.contains(extension)) {
            throw new RuntimeException("["+extension+"]文件类型不被接受");
        }

        // 记录分片上传
        saveFileChunkInfo(hash,totalSize,index,totalChunks,key,fileName);

        // 存储分片
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 获取文件存储服务
            FileStorageService storageService = storageProtocolService.getStorageService();
            // 保存文件
            storageService.saveChunk(inputStream, genChunkStoragePath(key, index));

            // 判断文件分片是否上传完成
            if (getChunksByFileKey(key).size() != totalChunks) {
                return new TAttach();
            }
            // 保存附件信息
            String storePath = DateUtil.format(new Date(), "yyyy/MM/dd");
            TAttach attach = saveAttachInfo(fileName, extension, totalSize, storageService.getProtocol(), storePath);

            // 合并分片上传
            storageService.mergeChunk(genStoragePath(attach), getTotalChunkPath(key, totalChunks));

            // 删除分片记录信息
            deleteFileChunkInfo(key);

            // 日志记录
            attachHandelLog(attach, AttachOptType.UPLOAD);

            return attach;
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败", e);
        }
    }

    /**
     * 判读当前分片是否已经上传过
     * @param chunkHash  分片hash
     * @param chunkIndex 分片索引
     * @param fileKey    文件唯一标识
     */
    private boolean currentChunkExist(String chunkHash, int chunkIndex, String fileKey) {
        List<TAttachChunk> attachChunks = getChunksByFileKey(fileKey);
        if (attachChunks !=null && !attachChunks.isEmpty()) {
            List<TAttachChunk> collect = attachChunks.stream()
                    .filter(item -> chunkHash.equals(item.getChunkHash()) && chunkIndex == item.getChunkIndex()).
                    collect(Collectors.toList());

            // 条件成立：当前分片已经上传过
            return !collect.isEmpty();
        }

        return false;
    }

    private List<TAttachChunk> getChunksByFileKey(String fileKey) {
        QueryWrapper<TAttachChunk> chunkQw = new QueryWrapper<>();
        chunkQw.eq("file_key", fileKey);
        return attachChunkMapper.selectList(chunkQw);
    }

    public void saveFileChunkInfo(String hash, int totalSize, int index, int totalChunks, String key, String fileName) {
        TAttachChunk chunk = new TAttachChunk();
        chunk.setChunkHash(hash);
        chunk.setChunkIndex(index);
        chunk.setChunkTotals(totalChunks);
        chunk.setFileKey(key);
        chunk.setFileTotalSize(totalSize);
        chunk.setFileName(fileName);
        chunk.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        chunk.setCreateTime(new Date());
        attachChunkMapper.insert(chunk);
    }
    public void deleteFileChunkInfo(String key) {
        UpdateWrapper<TAttachChunk> uw = new UpdateWrapper<>();
        uw.eq("file_key", key);
        attachChunkMapper.delete(uw);
    }

    // 保存附件信息
    private TAttach saveAttachInfo(String fileName, String extension, long size, String protocol, String storePath) {
        TAttach attach = new TAttach();
        attach.setOriginalName(fileName);
        attach.setAttachSize(size);
        attach.setExtension(extension);
        attach.setProtocol(protocol);
        attach.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        attach.setCreateTime(new Date());
        attach.setFileId(IdWorker.getId());
        attach.setPath(storePath);
        this.baseMapper.insert(attach);
        return attach;
    }

    @Override
    public void download(TAttach attach, OutputStream outputStream) {
        //获取文件存储服务
        FileStorageService storageService = storageProtocolService.getStorageService();

        if (!storageService.getProtocol().equals(attach.getProtocol())) {
            throw new RuntimeException("当前文件存储协议为" + attach.getProtocol() + "，与当前启用的存储配置不匹配");
        }

        try (InputStream inputStream = storageService.load(genStoragePath(attach))) {
            IoUtil.copy(inputStream, outputStream);
            attachHandelLog(attach, AttachOptType.DOWNLOAD);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    @Override
    public void batchDownload(List<Long> ids, OutputStream outputStream) {
        List<TAttach> tAttaches = listByIds(ids);
        List<Long> ids_ = tAttaches.stream().map(TAttach::getId).collect(Collectors.toList());
        if (Func.isEmpty(ids_)) {
            throw new RuntimeException("所选附件不存在");
        }

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            final FileStorageService storageService = storageProtocolService.getStorageService();

            // 过滤附件的存储协议
            List<String> protocols = tAttaches.stream().map(TAttach::getProtocol).distinct().collect(Collectors.toList());

            //存储协议校验
            if (protocols.size() != 1) {
                throw new RuntimeException("所选附件存存在多种存储协议");
            }
            if (!storageService.getProtocol().equals(protocols.get(0))) {
                throw new RuntimeException("所选文件存储协议为" + protocols.get(0) + "，与当前启用的存储配置不匹配");
            }

            // 下载附件并打成压缩包
            for (TAttach attach : tAttaches) {
                zipOutputStream.putNextEntry(new ZipEntry(attach.getOriginalName()));
                try (InputStream inputStream = storageService.load(genStoragePath(attach))) {
                    IoUtil.copy(inputStream, zipOutputStream);
                }
                attachHandelLog(attach, AttachOptType.DOWNLOAD);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败!", e);
        }
    }

    /**
     * 附件操作日志记录
     * @param attach 附件实体
     * @param type   操作类型
     */
    public void attachHandelLog(TAttach attach, AttachOptType type) {
        TAttachLog log = new TAttachLog();
        log.setFileId(attach.getFileId());
        log.setOriginalName(attach.getOriginalName());
        log.setExtension(attach.getExtension());
        log.setAttachSize(attach.getAttachSize());
        log.setOptUser(String.valueOf(AuthUtil.getUserId()));
        log.setOptUserName(AuthUtil.getUserName());
        log.setOptTime(new Date());
        log.setOptIp(WebUtil.getIP());
        log.setOptType(String.valueOf(type));
        attachLogService.save(log);
    }

    /**
     * 生成存储路径
     * @param attach 附件实体
     * @return 路径
     */
    private String genStoragePath(TAttach attach) {
        return String.format("%s/%s.%s", attach.getPath(), attach.getFileId(), attach.getExtension());
    }

    /**
     * 生成分片存储路径
     * @param key   唯一标识
     * @param index 当前分片索引
     */
    private String genChunkStoragePath(String key, int index) {
        String uniqueKey = key.replace(".", "_").replace("/","");
        return String.format("%s/%s.%s", "chunks", uniqueKey, "part" + index);
    }

    /**
     * 获取分片路径集合
     * @param key   唯一标识
     * @param total 总数
     */
    private LinkedList<String> getTotalChunkPath(String key, int total) {
        LinkedList<String> paths = new LinkedList<>();
        // 从0开始
        for (int i = 0; i < total; i++) {
            paths.add(genChunkStoragePath(key, i));
        }
        return paths;
    }

    public static double bytesToMB(long bytes) {
        return (double) bytes / (1024 * 1024);
    }
}
