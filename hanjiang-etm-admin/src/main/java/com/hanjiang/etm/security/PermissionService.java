package com.hanjiang.etm.security;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.auth.service.TMenuApiService;
import com.hanjiang.etm.auth.service.TRoleAuthService;
import com.hanjiang.etm.auth.service.TRoleService;
import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.entity.TMenuApi;
import com.hanjiang.etm.common.entity.TRole;
import com.hanjiang.etm.common.entity.TRoleAuth;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.CacheUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：API权限控制实现
 *
 * @author huxuehao
 **/
@Component("ps")
public class PermissionService {
    private final CacheUtil cacheUtil;
    private final TRoleService roleService;
    private final TMenuApiService menuApiService;
    private final TRoleAuthService roleAuthService;

    public PermissionService(CacheUtil cacheUtil, TRoleService roleService, TMenuApiService menuApiService, TRoleAuthService roleAuthService) {
        this.cacheUtil = cacheUtil;
        this.roleService = roleService;
        this.menuApiService = menuApiService;
        this.roleAuthService = roleAuthService;
    }

    /**
     * 验证当前用户是否有当前接口的权限
     * @param permissionCode 接口权限编号
     */
    public boolean hasPermission(String permissionCode) {
        // 放行登录接口
        if ("post::user:login".equals(permissionCode)) {
            return true;
        }

        // 尝试从缓存中获取
        Object authCodes = cacheUtil.get(Constant.API_AUTH_CACHE_PRE + AuthUtil.getUserId());
        if (authCodes != null) {
            List<String> authCodeList = JSON.parseArray(authCodes.toString(), String.class);
            return authCodeList.contains(permissionCode);
        }

        // 获取用户角色
        List<String> userRole = roleService.getUserRole(AuthUtil.getUserId());

        // 获取有效的用户角色
        QueryWrapper<TRole> roleQw = new QueryWrapper<>();
        roleQw.in("id", userRole);
        roleQw.eq("valid", 1);
        roleQw.eq("del_flag", 0);
        List<TRole> roleList = roleService.list(roleQw);
        userRole = roleList.stream().map(TRole::getId).map(Objects::toString).collect(Collectors.toList());

        if (userRole.isEmpty()) {
            return false;
        }

        // 处理超级管理员角色
        if (userRole.contains("1111111111111111111")) {
            QueryWrapper<TMenuApi> qw = new QueryWrapper<>();
            return verifyPermission(permissionCode, qw);
        }

        // 获取角色配置的API权限
        QueryWrapper<TRoleAuth> authQw = new QueryWrapper<>();
        authQw.in("role_id", userRole);
        authQw.eq("auth_type", 3);
        List<TRoleAuth> auths = roleAuthService.list(authQw);
        if (auths == null || auths.isEmpty()) {
            return false;
        }

        QueryWrapper<TMenuApi> qw = new QueryWrapper<>();
        qw.in("id", auths.stream().map(TRoleAuth::getAuthId).collect(Collectors.toList()));
        return verifyPermission(permissionCode, qw);
    }

    /**
     * 验证权限
     */
    private boolean verifyPermission(String permissionCode, QueryWrapper<TMenuApi> qw) {
        qw.eq("valid", 1);
        qw.eq("del_flag", 0);
        List<TMenuApi> validApiList = menuApiService.list(qw);
        List<String> validApis = validApiList.stream().map(TMenuApi::getCode).collect(Collectors.toList());

        // 保存缓存，有效期24小时
        cacheUtil.set(Constant.API_AUTH_CACHE_PRE + AuthUtil.getUserId(), JSON.toJSONString(validApis), 1000 * 60 * 60 * 24);

        return validApis.contains(permissionCode);
    }
}
