package com.hanjiang.etm.common.dto;

import lombok.Data;

/**
 * 描述：重置密码
 *
 * @author huxuehao
 **/
@Data
public class RePwdDto {
    private String oldPwd;
    private String newPwd;
    private String newRepwd;
}
