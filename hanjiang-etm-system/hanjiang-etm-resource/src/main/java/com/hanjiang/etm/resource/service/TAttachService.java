package com.hanjiang.etm.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TAttach;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * 描述：附件表
 *
 * @author huxuehao
 **/
public interface TAttachService extends IService<TAttach> {

    /**
     * 批量删除
     * @param ids 主键
     */
    boolean batchDeleteV2(List<Long> ids);

    /**
     * 上传
     * @param multipartFile file
     * @param originalFilename 源文件名
     */
    TAttach upload(MultipartFile multipartFile, String originalFilename);

    /**
     * 分片串行上传,并完成合并
     * @param multipartFile file片段
     * @param hash          file片段的哈希值
     * @param index         文件片段的索引
     * @param totalChunks   分片总数
     * @param key           唯一标识
     * @param fileName      文件名
     */
    TAttach uploadChunkAndMerge(MultipartFile multipartFile,
                        String hash,
                        int totalSize,
                        int index,
                        int totalChunks,
                        String key,
                        String fileName);

    /**
     * 下载
     * @param attach       附件实体
     * @param outputStream 输出流
     */
    void download(TAttach attach,OutputStream outputStream);

    /**
     * 批量现在
     * @param ids          附件ID集合
     * @param outputStream 输出流
     */
    void batchDownload(List<Long> ids, OutputStream outputStream);
}
