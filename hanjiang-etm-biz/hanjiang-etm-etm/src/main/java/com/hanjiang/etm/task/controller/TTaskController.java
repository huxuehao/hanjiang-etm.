package com.hanjiang.etm.task.controller;

import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TTask;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.task.service.TTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@MenuTag(code = "etm_task")
public class TTaskController {

    private final TTaskService tTaskService;

    public TTaskController(TTaskService taskService) {
        this.tTaskService = taskService;
    }

    @PreAuthorize("@ps.hasPermission('get::task:list')")
    @GetMapping(value = "list", name = "列表")
    public R<?> list(TTask tUserShow, String activeType) {
        return R.data(tTaskService.list(tUserShow,activeType));
    }

    @PreAuthorize("@ps.hasPermission('get::task:page')")
    @GetMapping(value = "page", name = "分页")
    public R<?> page(TTask tUserShow, PageParams pageParams, String activeType, Integer status) {
        return R.data(tTaskService.page(tUserShow,pageParams,activeType,status));
    }
}
