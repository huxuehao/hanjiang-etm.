package com.hanjiang.etm.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 描述：登录
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class LoginBody implements Serializable {
    private String account;
    private String password;
}
