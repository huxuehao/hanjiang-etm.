package com.hanjiang.etm.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：权限配置VO
 *
 * @author huxuehao
 **/
@Data
public class AuthConfig implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
    @JsonSerialize(contentUsing  = ToStringSerializer.class)
    private List<Long> menuIds;
    @JsonSerialize(contentUsing  = ToStringSerializer.class)
    private List<Long> buttonIds;
    @JsonSerialize(contentUsing  = ToStringSerializer.class)
    private List<Long> apiIds;
}
