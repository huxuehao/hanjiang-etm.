package com.hanjiang.etm.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TUserDto;
import com.hanjiang.etm.common.entity.LoginBody;
import com.hanjiang.etm.common.entity.TUser;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
public interface TUserService extends IService<TUser> {
    TUserDto validateLogin(LoginBody body);
    void logout(String id);
    TUserDto refreshToken(String refreshToken);
}
