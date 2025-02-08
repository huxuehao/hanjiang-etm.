package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.mp.annotation.QueryDefine;
import com.hanjiang.etm.common.mp.support.QueryCondition;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：参数表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_PARAMS)
public class TParams implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String paramName;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String paramKey;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String paramValue;

    @TableLogic
    private Integer delFlag;
}
