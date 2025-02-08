package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.mp.annotation.QueryDefine;
import com.hanjiang.etm.common.mp.support.QueryCondition;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：试题模版表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_QUESTION_TEMPLATE)
public class TQuestionTemplate implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long classifyId; // 所属分类ID

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer type; // 试题类型（1单选，2多选，3判断，4填空，5解答）

    @QueryDefine(condition = QueryCondition.LIKE)
    private String name; // 试题名称

    @QueryDefine(condition = QueryCondition.LIKE)
    private String title; // 题干

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer score; // 分数

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer level; // 难度等级

    private String config; // 试题配置

    private String createUser; // 创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer valid; // 是否有效（1有效，0无效）

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer share; // 是否共享（1共享，0不共享）

    @TableLogic
    private Integer delFlag; // 是否删除（1删除，0不删除）

    private Integer excel; // 是否来自于Excel导入（0否，1是）
}
