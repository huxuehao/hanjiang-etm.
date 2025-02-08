package com.hanjiang.etm.paper.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TPaperClassify;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.paper.service.TPaperClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/paper/classify")
@MenuTag(code = "etm_paper")
public class TPaperClassifyController {
    private final TPaperClassifyService paperClassifyService;

    public TPaperClassifyController(TPaperClassifyService paperClassifyService) {
        this.paperClassifyService = paperClassifyService;
    }

    @PreAuthorize("@ps.hasPermission('get::paper:classify:list')")
    @GetMapping(value = "list", name = "分类:列表")
    public R<?> list() {
        return R.data(paperClassifyService.list(new QueryWrapper<TPaperClassify>().eq("del_flag", 0)));
    }

    @PreAuthorize("@ps.hasPermission('get::paper:classify:tree')")
    @GetMapping(value = "tree", name = "分类:树形列表")
    public R<?> tree() {
        return R.data(paperClassifyService.listV2(MP.getQueryWrapper(new TPaperClassify())));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:classify:add')")
    @PostMapping(value = "add", name = "分类:新增")
    public R<?> add(@RequestBody TPaperClassify classify) {
        return R.data(paperClassifyService.save(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:classify:edit')")
    @PostMapping(value = "edit", name = "分类:编辑")
    public R<?> edit(@RequestBody TPaperClassify classify) {
        if (classify.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }
        if (classify.getParentId() == null) {
            classify.setParentId(0L);
        }
        return R.data(paperClassifyService.updateById(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:classify:delete')")
    @PostMapping(value = "delete", name = "分类:删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }
        return R.data(paperClassifyService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::paper:classify:select::id')")
    @GetMapping(value = "select/{id}", name = "分类:根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(paperClassifyService.getById(id));
    }
}
