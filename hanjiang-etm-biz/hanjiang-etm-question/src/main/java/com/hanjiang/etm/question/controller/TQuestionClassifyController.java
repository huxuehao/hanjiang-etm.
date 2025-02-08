package com.hanjiang.etm.question.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TQuestionClassify;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.question.service.TQuestionClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：试题分类
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/question/classify")
@MenuTag(code = "etm_question")
public class TQuestionClassifyController {
    private final TQuestionClassifyService questionClassifyService;

    public TQuestionClassifyController(TQuestionClassifyService questionClassifyService) {
        this.questionClassifyService = questionClassifyService;
    }

    @PreAuthorize("@ps.hasPermission('get::question:classify:list')")
    @GetMapping(value = "list", name = "分类:列表")
    public R<?> list() {
        return R.data(questionClassifyService.list(new QueryWrapper<TQuestionClassify>().eq("del_flag", 0)));
    }

    @PreAuthorize("@ps.hasPermission('get::question:classify:tree')")
    @GetMapping(value = "tree", name = "分类:树形列表")
    public R<?> tree() {
        return R.data(questionClassifyService.listV2(MP.getQueryWrapper(new TQuestionClassify())));
    }

    @PreAuthorize("@ps.hasPermission('post::question:classify:add')")
    @PostMapping(value = "add", name = "分类:新增")
    public R<?> add(@RequestBody TQuestionClassify classify) {
        return R.data(questionClassifyService.save(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::question:classify:edit')")
    @PostMapping(value = "edit", name = "分类:编辑")
    public R<?> edit(@RequestBody TQuestionClassify classify) {
        if (classify.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }
        if (classify.getParentId() == null) {
            classify.setParentId(0L);
        }
        return R.data(questionClassifyService.updateById(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::question:classify:delete')")
    @PostMapping(value = "delete", name = "分类:删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }
        return R.data(questionClassifyService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::question:classify:select::id')")
    @GetMapping(value = "select/{id}", name = "分类:根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {

        return R.data(questionClassifyService.getById(id));
    }
}
