package com.hanjiang.etm.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.auth.service.TMenuButtonService;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TMenuButton;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/menu-button")
@MenuTag(code = "system_btnmanager")
public class TMenuButtonController {
    private final TMenuButtonService menuButtonService;

    public TMenuButtonController(TMenuButtonService menuButtonService) {
        this.menuButtonService = menuButtonService;
    }

    @PreAuthorize("@ps.hasPermission('post::menu-button:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TMenuButton body) {
        QueryWrapper<TMenuButton> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        List<TMenuButton> list = menuButtonService.list(qw);
        if (list == null || list.isEmpty()) {
            body.setId(null);
            return R.data(menuButtonService.save(body));
        } else if(list.get(0).getCode().equals(body.getCode())) {
            return R.fail("按钮编号已存在");
        } else {
            return R.fail("未知字段重复");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::menu-button:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(menuButtonService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-button:valid')")
    @PostMapping(value = "/valid", name = "设置有效")
    public R<?> valid(@RequestBody List<Long> ids) {
        UpdateWrapper<TMenuButton> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("valid", 1);
        return R.data(menuButtonService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-button:unValid')")
    @PostMapping(value = "/unValid", name = "设置无效")
    public R<?> unValid(@RequestBody List<Long> ids) {
        UpdateWrapper<TMenuButton> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("valid", 0);
        return R.data(menuButtonService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::menu-button:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TMenuButton body) {
        QueryWrapper<TMenuButton> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.ne("id", body.getId());
        List<TMenuButton> list = menuButtonService.list(qw);
        if(list == null || list.isEmpty() || list.get(0).getId().equals(body.getId())) {
            return R.data(menuButtonService.updateById(body));
        } else if(list.get(0).getCode().equals(body.getCode())) {
            return R.fail("按钮编号已存在");
        } else {
            return R.fail("未知字段重复");
        }
    }

    @PreAuthorize("@ps.hasPermission('get::menu-button:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(TMenuButton user, PageParams pageParams) {
        return R.data(menuButtonService.page(MP.getPage(pageParams),MP.getQueryWrapper(user)));
    }

    @PreAuthorize("@ps.hasPermission('get::menu-button:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(menuButtonService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::menu-button:menu-button-tree')")
    @GetMapping(value = "/menu-button-tree", name = "菜单按钮树形列表")
    public R<?> tree() {
        return R.data(menuButtonService.tree());
    }
}
