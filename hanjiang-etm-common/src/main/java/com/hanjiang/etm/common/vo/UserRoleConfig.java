package com.hanjiang.etm.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：用户角色VO
 *
 * @author huxuehao
 **/
@Data
public class UserRoleConfig implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @JsonSerialize(contentUsing  = ToStringSerializer.class)
    private List<Long> roleIds;
}
