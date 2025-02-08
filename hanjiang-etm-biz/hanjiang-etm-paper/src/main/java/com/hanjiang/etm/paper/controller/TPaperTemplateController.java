package com.hanjiang.etm.paper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.dto.TPaperTemplateDto;
import com.hanjiang.etm.common.entity.TPaperTemplate;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.paper.service.TPaperTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：试卷
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/paper/template")
@MenuTag(code = "etm_paper")
public class TPaperTemplateController {
    private final TPaperTemplateService paperTemplateService;

    public TPaperTemplateController(TPaperTemplateService paperTemplateService) {
        this.paperTemplateService = paperTemplateService;
    }

    @PreAuthorize("@ps.hasPermission('get::paper:template:page')")
    @GetMapping(value = "page", name = "分页")
    public R<?> page(TPaperTemplate paperTemplate, PageParams pageParams) {
        if (paperTemplate == null) {
            paperTemplate = new TPaperTemplate();
        }
        // 超管时
        if ("administrator".equals(AuthUtil.getUserCode())) {
            paperTemplate.setCreateUser(null);
            paperTemplate.setShare(null);
        }
        // 共享时
        else if (paperTemplate.getShare() == 1) {
            paperTemplate.setCreateUser(null);
        }
        // 独享时
        else if (paperTemplate.getShare() == 0) {
            paperTemplate.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }
        // 空白时
        else {
            paperTemplate.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }

        IPage<TPaperTemplate> pageList = paperTemplateService.page(MP.getPage(pageParams), MP.getQueryWrapper(paperTemplate));
        return R.data(pageList);
    }

    @PreAuthorize("@ps.hasPermission('post::paper:template:add')")
    @PostMapping(value = "add", name = "新增")
    public R<?> add(@RequestBody TPaperTemplateDto paperTemplate) {
        return R.data(paperTemplateService.saveV2(paperTemplate));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:template:edit')")
    @PostMapping(value = "edit", name = "编辑")
    public R<?> edit(@RequestBody TPaperTemplateDto paperTemplate) {
        if (paperTemplate.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(paperTemplateService.updateByIdV2(paperTemplate));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:template:delete')")
    @PostMapping(value = "delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(paperTemplateService.removeBatchByIdsV2(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::paper:template:select::id')")
    @GetMapping(value = "select/{id}", name = "根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(paperTemplateService.getByIdV2(id));
    }
}
