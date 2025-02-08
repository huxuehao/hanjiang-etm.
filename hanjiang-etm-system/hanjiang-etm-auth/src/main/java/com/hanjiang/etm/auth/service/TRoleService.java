package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TRoleDto;
import com.hanjiang.etm.common.entity.TRole;
import com.hanjiang.etm.common.vo.AuthConfig;
import com.hanjiang.etm.common.vo.UserRoleConfig;

import java.util.List;

/**
 * 描述：角色
 *
 * @author huxuehao
 **/
public interface TRoleService extends IService<TRole> {
    List<TRoleDto> tree(TRole role);

    boolean deleteAllById(List<Long> ids);

    /**
     * 保存权限配置
     * @param config 权限配置
     */
    boolean saveAuthConfig(AuthConfig config);

    /**
     * 保存权限配置
     * @param roleId 角色ID
     */
    AuthConfig getAuthConfig(Long roleId);

    /**
     * 保存用户角色
     * @param config 配置
     */
    boolean saveUserRole(UserRoleConfig config);


    /**
     * 保存获取用户角色
     * @param userId 用户ID
     */
    List<String> getUserRole(Long userId);

    /**
     * 保存获取用户按钮权限
     */
    List<String> getButtonPermissions();

    /**
     * 保存获取用户角色
     * @param roleId 角色ID
     */
    List<String> getUserByRoleId(Long roleId);
}
