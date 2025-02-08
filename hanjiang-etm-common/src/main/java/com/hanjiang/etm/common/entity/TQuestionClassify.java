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
 * 描述：试题分类表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_QUESTION_CLASSIFY)
public class TQuestionClassify implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId; // 父ID

    @QueryDefine(condition = QueryCondition.LIKE)
    private String code; // 编号

    @QueryDefine(condition = QueryCondition.LIKE)
    private String name; // 名称

    @TableLogic
    private Integer delFlag; // 是否删除（0未删除，1删除）

    private Integer excel; // 是否来自于Excel导入（0否，1是）
}
