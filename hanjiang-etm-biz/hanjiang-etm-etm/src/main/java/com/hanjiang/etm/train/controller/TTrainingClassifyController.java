package com.hanjiang.etm.train.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TTrainingClassify;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.train.service.TTrainingClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/training/classify")
@MenuTag(code = "etm_train")
public class TTrainingClassifyController {
    private final TTrainingClassifyService trainingClassifyService;

    public TTrainingClassifyController(TTrainingClassifyService trainingClassifyService) {
        this.trainingClassifyService = trainingClassifyService;
    }

    @PreAuthorize("@ps.hasPermission('get::training:classify:page')")
    @GetMapping(value = "list", name = "分类:列表")
    public R<?> list() {
        return R.data(trainingClassifyService.list(new QueryWrapper<TTrainingClassify>().eq("del_flag", 0)));
    }

    @PreAuthorize("@ps.hasPermission('get::training:classify:tree')")
    @GetMapping(value = "tree", name = "分类:树形列表")
    public R<?> tree() {
        return R.data(trainingClassifyService.listV2(MP.getQueryWrapper(new TTrainingClassify())));
    }

    @PreAuthorize("@ps.hasPermission('post::training:classify:add')")
    @PostMapping(value = "add", name = "分类:新增")
    public R<?> add(@RequestBody TTrainingClassify classify) {
        return R.data(trainingClassifyService.save(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::training:classify:edit')")
    @PostMapping(value = "edit", name = "分类:编辑")
    public R<?> edit(@RequestBody TTrainingClassify classify) {
        if (classify.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }
        if (classify.getParentId() == null) {
            classify.setParentId(0L);
        }
        return R.data(trainingClassifyService.updateById(classify));
    }

    @PreAuthorize("@ps.hasPermission('post::training:classify:delete')")
    @PostMapping(value = "delete", name = "分类:删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }
        return R.data(trainingClassifyService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::training:classify:select::id')")
    @GetMapping(value = "select/{id}", name = "分类:根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(trainingClassifyService.getById(id));
    }
}
