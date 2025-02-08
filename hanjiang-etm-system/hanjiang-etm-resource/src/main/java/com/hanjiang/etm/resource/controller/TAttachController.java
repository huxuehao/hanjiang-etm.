package com.hanjiang.etm.resource.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TAttach;
import com.hanjiang.etm.common.entity.TAttachLog;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.resource.service.TAttachLogService;
import com.hanjiang.etm.resource.service.TAttachService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 描述：附件表
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/attach")
@MenuTag(code = "resource_attach")
public class TAttachController {
    private final TAttachService attachService;
    private final TAttachLogService attachLogService;

    public TAttachController(TAttachService attachService, TAttachLogService attachLogService) {
        this.attachService = attachService;
        this.attachLogService = attachLogService;
    }

    @PreAuthorize("@ps.hasPermission('get::attach:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(TAttach attach, PageParams pageParams) {
        QueryWrapper<TAttach> qw = MP.getQueryWrapper(attach);
        qw.last("ORDER BY create_time DESC");
        return R.data(attachService.page(MP.getPage(pageParams), qw));
    }

    @PreAuthorize("@ps.hasPermission('get::attach:log:page')")
    @GetMapping(value = "/log/page", name = "日志:分页")
    public R<?> LogPage(TAttachLog attachLog, PageParams pageParams) {
        QueryWrapper<TAttachLog> qw = MP.getQueryWrapper(attachLog);
        qw.last("ORDER BY opt_time DESC");
        return R.data(attachLogService.page(MP.getPage(pageParams), qw));
    }

    @PreAuthorize("@ps.hasPermission('post::attach:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(attachService.batchDeleteV2(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::attach:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(attachService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('post::attach:list')")
    @PostMapping(value = "/list", name = "列表")
    public R<?> selectList(@RequestBody List<Long> ids) {
        return R.data(attachService.listByIds(ids));
    }

    /**
     * 上传
     */
    @PreAuthorize("@ps.hasPermission('post::attach:upload')")
    @PostMapping(value = "/upload", name = "上传")
    public R<?> upload(@RequestParam("file") MultipartFile file) {
        final TAttach attach = attachService.upload(file, file.getOriginalFilename());
        return R.data(String.valueOf(attach.getId()));
    }

    /**
     * 分片串行上传
     */
    @PreAuthorize("@ps.hasPermission('post::attach:chunk-upload')")
    @PostMapping(value = "/chunk-upload", name = "分片串行上传")
    public R<?> uploadChunk(@RequestParam("file") MultipartFile file,
                              @RequestParam("hash") String hash,
                              @RequestParam("totalSize") int totalSize,
                              @RequestParam("index") int index,
                              @RequestParam("totalChunks") int totalChunks,
                              @RequestParam("uniqueKey") String uniqueKey,
                              @RequestParam("fileName") String fileName) {
        TAttach attach = attachService.uploadChunkAndMerge(file, hash, totalSize, index, totalChunks, uniqueKey, fileName);
        return R.data(String.valueOf(attach.getId()));
    }

    /**
     * 下载
     */
    @PreAuthorize("@ps.hasPermission('get::attach:download::id')")
    @GetMapping(value = "download/{id}", name = "下载")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) {
        TAttach attach = attachService.getById(id);
        if (Func.isEmpty(attach)) {
            throw new RuntimeException("附件不存在");
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename=" + URLEncoder.encode(attach.getOriginalName(), "UTF-8"));
            this.attachService.download(attach, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 批量下载
     */
    @PreAuthorize("@ps.hasPermission('post::attach:batchDownload')")
    @PostMapping(value = "batchDownload", name = "批量下载")
    public void batchDownload(@RequestBody List<Long> ids, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode("批量下载.zip"
                    , "UTF-8"));
            this.attachService.batchDownload(ids, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败！", e);
        }
    }
}
