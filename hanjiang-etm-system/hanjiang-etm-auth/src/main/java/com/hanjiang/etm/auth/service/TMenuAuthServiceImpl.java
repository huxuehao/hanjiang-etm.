package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TMenuDto;
import com.hanjiang.etm.common.entity.TMenu;
import com.hanjiang.etm.common.entity.TRole;
import com.hanjiang.etm.common.entity.TRoleAuth;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.menu.mapper.TMenuMapper;
import com.hanjiang.etm.menu.service.TMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：菜单权限
 *
 * @author huxuehao
 **/
@Service
public class TMenuAuthServiceImpl implements TMenuAuthService {
    private final TRoleService roleService;
    private final TMenuService menuService;
    private final TMenuMapper menuMapper;
    private final TRoleAuthService roleAuthService;

    public TMenuAuthServiceImpl(TRoleService roleService, TMenuService menuService, TMenuMapper menuMapper, TRoleAuthService roleAuthService) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.menuMapper = menuMapper;
        this.roleAuthService = roleAuthService;
    }

    @Override
    public List<TMenuDto> permissionTree() {
        List<String> userRole = roleService.getUserRole(AuthUtil.getUserId());
        // 获取有效的用户角色
        QueryWrapper<TRole> roleQw = new QueryWrapper<>();
        roleQw.in("id", userRole);
        roleQw.eq("valid", 1);
        roleQw.eq("del_flag", 0);
        List<TRole> roleList = roleService.list(roleQw);
        userRole = roleList.stream().map(TRole::getId).map(Objects::toString).collect(Collectors.toList());

        if (userRole.isEmpty()) {
            return new ArrayList<>();
        }

        // 处理超级管理员角色
        if (userRole.contains("1111111111111111111")) {
            return menuService.tree(new TMenu());
        }

        // 获取角色配置的菜单权限
        QueryWrapper<TRoleAuth> authQw = new QueryWrapper<>();
        authQw.in("role_id", userRole);
        authQw.eq("auth_type", 1);
        List<TRoleAuth> auths = roleAuthService.list(authQw);
        if (auths == null || auths.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取有效的菜单
        QueryWrapper<TMenu> qw = new QueryWrapper<>();
        qw.in("id", auths.stream().map(TRoleAuth::getAuthId).collect(Collectors.toList()));
        qw.eq("valid", 1);
        qw.eq("del_flag", 0);
        List<TMenu> validMenu = menuService.list(qw);
        List<Long> validMenuIds = validMenu.stream().map(TMenu::getId).collect(Collectors.toList());

        // 获取TMenuDto列表
        List<TMenuDto> list = menuMapper.listV2(DBConst.T_MENU, MP.getQueryWrapper(new TMenu()));
        // 过滤有效菜单
        List<TMenuDto> permissionMenuList = list.stream().filter(item -> validMenuIds.contains(item.id)).collect(Collectors.toList());

        return TreeUtil.convertTree(permissionMenuList);
    }

}
