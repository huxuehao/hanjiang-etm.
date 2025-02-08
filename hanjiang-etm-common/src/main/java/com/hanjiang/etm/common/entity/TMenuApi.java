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
 * 描述：菜单接口表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_MENU_API)
public class TMenuApi implements Serializable {
    static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    public Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long menuId;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String name;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String code;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String path;

    @QueryDefine(condition = QueryCondition.EQ)
    private String method;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer valid; // 是否有效（1有效，0无效）

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer type; // 类型（1自动扫描，0手动添加）

    @JsonSerialize(using = ToStringSerializer.class)
    private Long nextTimeMillis;

    @TableLogic
    Integer delFlag; // 是否删除（1删除，0未删除）
}
