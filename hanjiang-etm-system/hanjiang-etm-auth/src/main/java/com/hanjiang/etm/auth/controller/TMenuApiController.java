package com.hanjiang.etm.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.auth.service.TMenuApiService;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TMenuApi;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/menu-api")
@MenuTag(code = "system_apimanager")
public class TMenuApiController {
    private final TMenuApiService menuApiService;

    public TMenuApiController(TMenuApiService menuApiService) {
        this.menuApiService = menuApiService;
    }

    @PreAuthorize("@ps.hasPermission('post::menu-api:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TMenuApi body) {
        QueryWrapper<TMenuApi> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        List<TMenuApi> list = menuApiService.list(qw);
        if (list == null || list.isEmpty()) {
            body.setId(null);
            body.setNextTimeMillis(System.currentTimeMillis());
            return R.data(menuApiService.save(body));
        } else if(list.get(0).getCode().equals(body.getCode())) {
            return R.fail("接口编号已存在");
        } else {
            return R.fail("未知字段重复");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::menu-api:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(menuApiService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-api:valid')")
    @PostMapping(value = "/valid", name = "设置有效")
    public R<?> valid(@RequestBody List<Long> ids) {
        UpdateWrapper<TMenuApi> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("valid", 1);
        uw.set("next_time_millis", System.currentTimeMillis());
        return R.data(menuApiService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-api:unValid')")
    @PostMapping(value = "/unValid", name = "设置无效")
    public R<?> unValid(@RequestBody List<Long> ids) {
        UpdateWrapper<TMenuApi> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("valid", 0);
        uw.set("next_time_millis", System.currentTimeMillis());
        return R.data(menuApiService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-api:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TMenuApi body) {
        QueryWrapper<TMenuApi> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.ne("id", body.getId());
        List<TMenuApi> list = menuApiService.list(qw);
        if(list == null || list.isEmpty() || list.get(0).getId().equals(body.getId())) {
            body.setNextTimeMillis(System.currentTimeMillis());
            return R.data(menuApiService.updateById(body));
        } else if(list.get(0).getCode().equals(body.getCode())) {
            return R.fail("接口编号已存在");
        } else {
            return R.fail("未知字段重复");
        }
    }

    @PreAuthorize("@ps.hasPermission('get::menu-api:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> tree(TMenuApi user, PageParams pageParams) {
        return R.data(menuApiService.page(MP.getPage(pageParams),MP.getQueryWrapper(user)));
    }

    @PreAuthorize("@ps.hasPermission('get::menu-api:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(menuApiService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::menu-api:menu-button-tree')")
    @GetMapping(value = "/menu-button-tree", name = "菜单接口树形列表")
    public R<?> tree() {
        return R.data(menuApiService.tree());
    }
}
