package com.hanjiang.etm.auth.service;

import com.hanjiang.etm.common.dto.TMenuDto;

import java.util.List;

/**
 * 描述：菜单权限
 *
 * @author huxuehao
 **/
public interface TMenuAuthService {
    List<TMenuDto> permissionTree();
}
