package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.auth.mapper.TRoleMapper;
import com.hanjiang.etm.auth.mapper.TUserRoleMapper;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TRoleDto;
import com.hanjiang.etm.common.entity.TMenuButton;
import com.hanjiang.etm.common.entity.TRole;
import com.hanjiang.etm.common.entity.TRoleAuth;
import com.hanjiang.etm.common.entity.TUserRole;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.common.vo.AuthConfig;
import com.hanjiang.etm.common.vo.UserRoleConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：角色
 *
 * @author huxuehao
 **/
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole> implements TRoleService {
    private final TRoleAuthService roleAuthService;
    private final TUserRoleMapper userRoleMapper;
    private final TMenuButtonService menuButtonService;

    public TRoleServiceImpl(TRoleAuthService roleAuthService, TUserRoleMapper userRoleMapper, TMenuButtonService menuButtonService) {
        this.roleAuthService = roleAuthService;
        this.userRoleMapper = userRoleMapper;
        this.menuButtonService = menuButtonService;
    }

    @Override
    public List<TRoleDto> tree(TRole role) {
        List<TRoleDto> list = baseMapper.listV2(DBConst.T_ROLE, MP.getQueryWrapper(role));
        return TreeUtil.convertTree(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAllById(List<Long> ids) {
        QueryWrapper<TRole> qw = new QueryWrapper<>();
        qw.in("parent_id", ids);
        qw.eq("del_flag", 0);
        List<TRole> roleList = list(qw);

        if (Func.isEmpty(roleList)) {
            return removeBatchByIds(ids);
        }
        // 有子级的ID集合
        List<Long> hasChildIds = roleList.stream().map(TRole::getParentId).collect(Collectors.toList());
        // 没有子级的ID集合
        List<Long> noneChildIds = ids.stream().filter(id -> !hasChildIds.contains(id)).collect(Collectors.toList());
        removeBatchByIds(noneChildIds);

        return deleteAllById(hasChildIds);
    }

    @Override
    @Transactional
    public boolean saveAuthConfig(AuthConfig config) {
        Long roleId = config.getRoleId();
        List<Long> menuIds = config.getMenuIds();
        List<Long> buttonIds = config.getButtonIds();
        List<Long> apiIds = config.getApiIds();

        ArrayList<TRoleAuth> menuRoleAuths = new ArrayList<>();
        for (Long menuId : menuIds) {
            TRoleAuth menuRoleAuth = new TRoleAuth();
            menuRoleAuth.setRoleId(roleId);
            menuRoleAuth.setAuthId(menuId);
            menuRoleAuth.setAuthType(1);
            menuRoleAuths.add(menuRoleAuth);
        }

        ArrayList<TRoleAuth> buttonRoleAuths = new ArrayList<>();
        for (Long buttonId : buttonIds) {
            TRoleAuth buttonRoleAuth = new TRoleAuth();
            buttonRoleAuth.setRoleId(roleId);
            buttonRoleAuth.setAuthId(buttonId);
            buttonRoleAuth.setAuthType(2);
            buttonRoleAuths.add(buttonRoleAuth);
        }

        ArrayList<TRoleAuth> apiRoleAuths = new ArrayList<>();
        for (Long apiId : apiIds) {
            TRoleAuth apiRoleAuth = new TRoleAuth();
            apiRoleAuth.setRoleId(roleId);
            apiRoleAuth.setAuthId(apiId);
            apiRoleAuth.setAuthType(3);
            apiRoleAuths.add(apiRoleAuth);
        }

        QueryWrapper<TRoleAuth> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        roleAuthService.remove(qw);

        if (!menuRoleAuths.isEmpty()) {
            roleAuthService.saveBatch(menuRoleAuths);
        }

        if (!buttonRoleAuths.isEmpty()) {
            roleAuthService.saveBatch(buttonRoleAuths);
        }

        if (!apiRoleAuths.isEmpty()) {
            roleAuthService.saveBatch(apiRoleAuths);
        }

        return true;
    }

    @Override
    public AuthConfig getAuthConfig(Long roleId) {
        QueryWrapper<TRoleAuth> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        List<TRoleAuth> list = roleAuthService.list(qw);

        AuthConfig authConfig = new AuthConfig();
        authConfig.setRoleId(roleId);

        List<Long> menuIds = list.stream()
                .filter(item -> item.getAuthType() == 1)
                .map(TRoleAuth::getAuthId)
                .collect(Collectors.toList());
        authConfig.setMenuIds(menuIds);

        List<Long> buttonIds = list.stream()
                .filter(item -> item.getAuthType() == 2)
                .map(TRoleAuth::getAuthId)
                .collect(Collectors.toList());
        authConfig.setButtonIds(buttonIds);

        List<Long> apiIds = list.stream()
                .filter(item -> item.getAuthType() == 3)
                .map(TRoleAuth::getAuthId)
                .collect(Collectors.toList());
        authConfig.setApiIds(apiIds);

        return authConfig;
    }

    @Override
    @Transactional
    public boolean saveUserRole(UserRoleConfig config) {
        Long userId = config.getUserId();
        List<Long> roleIds = config.getRoleIds();

        QueryWrapper<TUserRole> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        userRoleMapper.delete(qw);

        for (Long roleId : roleIds) {
            TUserRole userRole = new TUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            userRoleMapper.insert(userRole);
        }

        return true;
    }

    @Override
    public List<String> getUserRole(Long userId) {
        QueryWrapper<TUserRole> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        List<TUserRole> userRoles = userRoleMapper.selectList(qw);
        return userRoles.stream()
                .map(TUserRole::getRoleId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getButtonPermissions() {
        // 获取用户配置的角色
        List<String> userRole = getUserRole(AuthUtil.getUserId());

        // 获取有效的用户角色
        QueryWrapper<TRole> roleQw = new QueryWrapper<>();
        roleQw.in("id", userRole);
        roleQw.eq("valid", 1);
        roleQw.eq("del_flag", 0);
        List<TRole> roleList = list(roleQw);
        userRole = roleList.stream().map(TRole::getId).map(Objects::toString).collect(Collectors.toList());

        if (userRole.isEmpty()) {
            return new ArrayList<>();
        }

        // 处理超级管理员角色
        if (userRole.contains("1111111111111111111")) {
            QueryWrapper<TMenuButton> qw = new QueryWrapper<>();
            qw.eq("valid", 1);
            qw.eq("del_flag", 0);
            List<TMenuButton> validButton = menuButtonService.list(qw);

            return validButton.stream().map(TMenuButton::getCode).collect(Collectors.toList());
        }

        // 获取角色配置的按钮权限
        QueryWrapper<TRoleAuth> authQw = new QueryWrapper<>();
        authQw.in("role_id", userRole);
        authQw.eq("auth_type", 2);
        List<TRoleAuth> auths = roleAuthService.list(authQw);
        if (auths == null || auths.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取有效的按钮配置
        QueryWrapper<TMenuButton> qw = new QueryWrapper<>();
        qw.in("id", auths.stream().map(TRoleAuth::getAuthId).collect(Collectors.toList()));
        qw.eq("valid", 1);
        qw.eq("del_flag", 0);
        List<TMenuButton> validButton = menuButtonService.list(qw);

        return validButton.stream().map(TMenuButton::getCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserByRoleId(Long roleId) {
        QueryWrapper<TUserRole> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId);
        List<TUserRole> userRoles = userRoleMapper.selectList(qw);
        return userRoles.stream()
                .map(TUserRole::getUserId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
