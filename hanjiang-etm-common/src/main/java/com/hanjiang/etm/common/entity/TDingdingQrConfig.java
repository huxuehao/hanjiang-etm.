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
 * 描述：钉钉配置表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_DINGDING_QR_CONFIG)
public class TDingdingQrConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long id;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String name;
    @QueryDefine(condition = QueryCondition.EQ)
    private String miniAppId;
    @QueryDefine(condition = QueryCondition.EQ)
    private String agentId;
    @QueryDefine(condition = QueryCondition.EQ)
    private String corpId;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String page;
    @QueryDefine(condition = QueryCondition.EQ)
    private Integer enabled;
    @TableLogic
    private Integer delFlag;
}
