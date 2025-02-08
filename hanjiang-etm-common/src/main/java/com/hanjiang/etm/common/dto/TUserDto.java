package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
@Setter
@Getter
public class TUserDto extends TUser {
    private String accessToken;
    private String refreshToken;
}
