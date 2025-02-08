package com.hanjiang.etm.params.controller;

import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TParams;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.params.service.TParamsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/params")
@MenuTag(code = "system_params")
public class TParamsController {
    private final TParamsService paramsService;

    public TParamsController(TParamsService paramsService) {
        this.paramsService = paramsService;
    }

    @PreAuthorize("@ps.hasPermission('get::params:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(TParams params, PageParams pageParams) {
        return R.data(paramsService.page(MP.getPage(pageParams), MP.getQueryWrapper(params)));
    }

    @PreAuthorize("@ps.hasPermission('post::params:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TParams params) {
        return R.data(paramsService.saveV2(params));
    }

    @PreAuthorize("@ps.hasPermission('post::params:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TParams params) {
        return R.data(paramsService.updateByIdV2(params));
    }

    @PreAuthorize("@ps.hasPermission('post::params:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(paramsService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::params:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(paramsService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::params:fetch-value-by-key')")
    @GetMapping(value = "/fetch-value-by-key", name = "根据key获取value")
    public R<?> fetchValueByKey(@RequestParam String key) {
        return R.data(paramsService.fetchValueByKey(key));
    }

}
