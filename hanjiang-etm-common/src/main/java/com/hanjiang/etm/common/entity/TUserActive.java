package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：考试表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_USER_ACTIVE)
public class TUserActive implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examTranId;

    private String userCode;

    private String activeTime;

    private Integer activeType;
}
