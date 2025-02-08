package com.hanjiang.etm.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TExamClassify;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.exam.service.TExamClassifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/exam/classify")
@MenuTag(code = "etm_exam")
public class TExamClassifyController {
    private final TExamClassifyService tExamClassifyService;

    public TExamClassifyController(TExamClassifyService tExamClassifyService){
        this.tExamClassifyService = tExamClassifyService;
    }

    @PreAuthorize("@ps.hasPermission('get::exam:classify:tree')")
    @GetMapping(value = "/tree", name = "分类:树形列表")
    public R<?> tree() {
        return R.data(tExamClassifyService.examTree(MP.getQueryWrapper(new TExamClassify())));
    }

    //考试分类新增
    @PreAuthorize("@ps.hasPermission('post::exam:classify:add')")
    @PostMapping(value = "/add", name = "分类:新增")
    public R<?> examAdd(@RequestBody TExamClassify tExamClassify){
        return R.data(tExamClassifyService.save(tExamClassify));
    }

    //考试分类编辑
    @PreAuthorize("@ps.hasPermission('post::exam:classify:edit')")
    @PostMapping(value = "/edit", name = "分类:编辑")
    public R<?> examEdit(@RequestBody TExamClassify classify){
        if(classify.getId() == null){
            throw new RuntimeException("ID不能为空");
        }
        if (classify.getParentId() == null) {
            classify.setParentId(0L);
        }
        return R.data(tExamClassifyService.updateById(classify));
    }

    //考试分类删除
    @PreAuthorize("@ps.hasPermission('post::exam:classify:delete')")
    @PostMapping(value = "/delete", name = "分类:删除")
    public R<?> examDelete(@RequestBody List<Long> ids){
        if(ids == null || ids.isEmpty()){
            throw new RuntimeException("ID不能为空");
        }
        return R.data(tExamClassifyService.removeBatchByIds(ids));
    }

    //分类
    @PreAuthorize("@ps.hasPermission('get::exam:classify:list')")
    @GetMapping(value = "/list", name = "分类:列表")
    public R<?> list(){
        return R.data(tExamClassifyService.list(new QueryWrapper<TExamClassify>().eq("del_flag",0)));
    }
}
