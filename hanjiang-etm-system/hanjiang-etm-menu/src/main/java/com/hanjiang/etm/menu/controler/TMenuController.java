package com.hanjiang.etm.menu.controler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TMenu;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.menu.service.TMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：菜单
 *
 * @author huxuehao
 **/

@RestController
@RequestMapping("/menu")
@MenuTag(code = "system_menu")
public class TMenuController {
    private final TMenuService menuService;

    public TMenuController(TMenuService menuService) {
        this.menuService = menuService;
    }

    @PreAuthorize("@ps.hasPermission('post::menu:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TMenu body) {
        QueryWrapper<TMenu> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        List<TMenu> list = menuService.list(qw);
        if (list == null || list.isEmpty()) {
            return R.data(menuService.save(body));
        } else {
            return R.fail("组织编码已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::menu:delete')")
    @PostMapping(value="/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(menuService.deleteAllById(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::menu:set-valid')")
    @PostMapping(value = "/set-valid", name = "设置有效接口")
    public R<?> delete(@RequestBody List<Long> ids, @RequestParam int valid) {
        UpdateWrapper<TMenu> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.eq("del_flag", 0);
        uw.set("valid", valid);

        return R.data(menuService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::menu:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TMenu body) {
        QueryWrapper<TMenu> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.ne("id", body.getId());
        List<TMenu> list = menuService.list(qw);
        if (list == null || list.isEmpty()) {
            if (body.getParentId() == null) {
                body.setParentId(0L);
            }
            return R.data(menuService.updateById(body));
        } else {
            return R.fail("菜单编码已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('get::menu:tree')")
    @GetMapping(value = "/tree", name = "树形列表")
    public R<?> tree(TMenu menu) {
        return R.data(menuService.tree(menu));
    }

    @PreAuthorize("@ps.hasPermission('get::menu:list')")
    @GetMapping(value = "/list", name = "列表")
    public R<?> list(TMenu menu) {
        return R.data(menuService.list(MP.getQueryWrapper(menu)));
    }

    @PreAuthorize("@ps.hasPermission('get::menu:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(menuService.getById(id));
    }
}
