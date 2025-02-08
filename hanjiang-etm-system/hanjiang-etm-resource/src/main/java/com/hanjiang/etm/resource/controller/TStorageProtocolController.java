package com.hanjiang.etm.resource.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TStorageProtocol;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.resource.service.TStorageProtocolService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 描述：文件存储协议配置
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/storage")
@MenuTag(code = "resource_storage")
public class TStorageProtocolController {
    private final TStorageProtocolService storageProtocolService;

    public TStorageProtocolController(TStorageProtocolService storageProtocolService) {
        this.storageProtocolService = storageProtocolService;
    }

    @PreAuthorize("@ps.hasPermission('post::storage:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TStorageProtocol body) {
        body.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        body.setCreateTime(new Date());
        return R.data(storageProtocolService.saveV2(body));
    }

    @PreAuthorize("@ps.hasPermission('post::storage:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TStorageProtocol body) {
        return R.data(storageProtocolService.updateV2(body));
    }

    @PreAuthorize("@ps.hasPermission('post::storage:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(storageProtocolService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::storage:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(TStorageProtocol storageProtocol, PageParams pageParams) {
        return R.data(storageProtocolService.page(MP.getPage(pageParams), MP.getQueryWrapper(storageProtocol)));
    }

    @PreAuthorize("@ps.hasPermission('get::storage:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(storageProtocolService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::storage:validSuccess')")
    @GetMapping(value = "/validSuccess", name = "设置有效")
    public R<?> validSuccess(@RequestParam Long id) {
        return R.data(storageProtocolService.validSuccess(id));
    }

    @PreAuthorize("@ps.hasPermission('post::storage:updateProtocol')")
    @PostMapping(value = "/updateProtocol", name = "更新协议配置")
    public R<?> updateProtocol(@RequestBody TStorageProtocol body) {
        UpdateWrapper<TStorageProtocol> uw = new UpdateWrapper<>();
        uw.eq("id", body.getId());
        uw.set("protocol_config", body.getProtocolConfig());
        uw.set("update_user", String.valueOf(AuthUtil.getUserId()));
        uw.set("update_time", new Date());
        return R.data(storageProtocolService.update(uw));
    }
}
