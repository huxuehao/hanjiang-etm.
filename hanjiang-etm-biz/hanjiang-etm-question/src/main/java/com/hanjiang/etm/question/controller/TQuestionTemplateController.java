package com.hanjiang.etm.question.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TQuestionTemplate;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.vo.ImportQuestionParserBoxVo;
import com.hanjiang.etm.question.excelImport.ImportParserService;
import com.hanjiang.etm.question.service.TQuestionClassifyService;
import com.hanjiang.etm.question.service.TQuestionTemplateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * 描述：试题模版
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/question/template")
@MenuTag(code = "etm_question")
public class TQuestionTemplateController {
    private final TQuestionTemplateService questionTemplateService;
    private final TQuestionClassifyService questionClassifyService;
    private final ImportParserService importParserService;

    public TQuestionTemplateController(TQuestionTemplateService questionTemplateService, TQuestionClassifyService questionClassifyService, ImportParserService importParserService) {
        this.questionTemplateService = questionTemplateService;
        this.questionClassifyService = questionClassifyService;
        this.importParserService = importParserService;
    }

    @PreAuthorize("@ps.hasPermission('get::question:template:page')")
    @GetMapping(value = "page", name = "分页")
    public R<?> page(TQuestionTemplate questionTemplate, PageParams pageParams) {
        if (questionTemplate == null) {
            questionTemplate = new TQuestionTemplate();
        }
        // 超管时
        if ("administrator".equals(AuthUtil.getUserCode())) {
            questionTemplate.setCreateUser(null);
            questionTemplate.setShare(null);
        }
        // 共享时
        else if (questionTemplate.getShare() == 1) {
            questionTemplate.setCreateUser(null);
        }
        // 独享时
        else if (questionTemplate.getShare() == 0) {
            questionTemplate.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }
        // 空白时
        else {
            questionTemplate.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }

        IPage<TQuestionTemplate> pageList = questionTemplateService.page(MP.getPage(pageParams), MP.getQueryWrapper(questionTemplate));
        return R.data(pageList);
    }

    @PreAuthorize("@ps.hasPermission('post::question:template:add')")
    @PostMapping(value = "add", name = "新增")
    public R<?> add(@RequestBody TQuestionTemplate questionTemplate) {
        questionTemplate.setCreateTime(new Date());
        questionTemplate.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        return R.data(questionTemplateService.save(questionTemplate));
    }

    @PreAuthorize("@ps.hasPermission('post::question:template:edit')")
    @PostMapping(value = "edit", name = "编辑")
    public R<?> edit(@RequestBody TQuestionTemplate questionTemplate) {
        if (questionTemplate.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(questionTemplateService.updateById(questionTemplate));
    }

    @PreAuthorize("@ps.hasPermission('post::question:template:delete')")
    @PostMapping(value = "delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(questionTemplateService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::question:template:select::id')")
    @GetMapping(value = "select/{id}", name = "根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(questionTemplateService.getById(id));
    }


    /**
     * 解析Excel，此操作为导入的预操作
     * @param file 用户上传的文件
     */
    @PreAuthorize("@ps.hasPermission('post::question:template:file-parser')")
    @PostMapping(value = "/file-parser", name = "解析Excel")
    public R<?> parserFile(@RequestBody MultipartFile file) throws IOException {
        return R.data(importParserService.parserExcelToQuestion(file));
    }

    @PreAuthorize("@ps.hasPermission('get::question:template:downloadTemplate')")
    @GetMapping(value = "/downloadTemplate", name = "下载模版")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        // 获取文件资源
        Resource resource = new ClassPathResource("question_template.xlsx");

        // 判断文件是否存在
        if (!resource.exists()) {
            throw new RuntimeException("模本不存在");
        }

        // 设置响应头
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"试题导入模本.xlsx\"");
        response.setContentType("application/octet-stream"); // 设置文件类型，通用二进制流

        // 获取文件的输入流
        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    /**
     * 导入
     */
    @PreAuthorize("@ps.hasPermission('get::question:template:submitImport')")
    @PostMapping(value = "/submitImport", name = "导入")
    @Transactional
    public R<?> submitImport(@RequestBody ImportQuestionParserBoxVo importQuestion) {
        questionTemplateService.submitImport(importQuestion);
        return R.data(true);
    }

    /**
     * 取消导入
     */
    @PreAuthorize("@ps.hasPermission('get::question:template:cancelImport')")
    @PostMapping(value = "/cancelImport", name = "取消导入")
    public R<?> cancelImport(@RequestBody ImportQuestionParserBoxVo importQuestion) {
        // 删除新加的分类
        List<Long> classifyIds0 = importQuestion.getSingle().getClassifyIds();
        if (classifyIds0 == null || !classifyIds0.isEmpty()) {
            questionClassifyService.removeBatchByIds(classifyIds0);
        }
        // 删除新加的分类
        List<Long> classifyIds1 = importQuestion.getMultiple().getClassifyIds();
        if (classifyIds1 == null || !classifyIds1.isEmpty()) {
            questionClassifyService.removeBatchByIds(classifyIds1);
        }
        // 删除新加的分类
        List<Long> classifyIds2 = importQuestion.getJudge().getClassifyIds();
        if (classifyIds2 == null || !classifyIds2.isEmpty()) {
            questionClassifyService.removeBatchByIds(classifyIds2);
        }
        // 删除新加的分类
        List<Long> classifyIds3 = importQuestion.getCompletion().getClassifyIds();
        if (classifyIds3 == null || !classifyIds3.isEmpty()) {
            questionClassifyService.removeBatchByIds(classifyIds3);
        }
        // 删除新加的分类
        List<Long> classifyIds4 = importQuestion.getQa().getClassifyIds();
        if (classifyIds4 == null || !classifyIds4.isEmpty()) {
            questionClassifyService.removeBatchByIds(classifyIds4);
        }
        return R.data(true);
    }
}
