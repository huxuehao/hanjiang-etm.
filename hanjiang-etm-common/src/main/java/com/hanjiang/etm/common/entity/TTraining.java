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
 * 描述：培训表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_TRAIN)
public class TTraining implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long classifyId; // 培训分类ID

    @QueryDefine(condition = QueryCondition.LIKE)
    private String title; // 培训标题

    private String des; // 培训描述
    private Integer duration; // 答题时长
    private Integer qualifiedScore; // 合格分数

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.GE)
    private Date limitStartTime; // 培训开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.LE)
    private Date limitEndTime; // 培训结束时间

    @QueryDefine(condition = QueryCondition.EQ)
    private String createUser; // 创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer published; // 是否发布（1已发布，0未发布）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.BETWEEN)
    private Date publishTime; // 发布时间

    @TableLogic
    private Integer delFlag; // 是否删除（1删除，0未删除）
}
