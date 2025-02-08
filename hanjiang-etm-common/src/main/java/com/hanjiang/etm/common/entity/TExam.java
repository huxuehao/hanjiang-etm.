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
 * 描述：考试表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_EXAM)
public class TExam implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long classifyId; // 分类

    @QueryDefine(condition = QueryCondition.LIKE)
    private String name; // 名称

    @QueryDefine(condition = QueryCondition.LIKE)
    private String code; // 编号

    @QueryDefine(condition = QueryCondition.EQ)
    private String createUser; // 创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间

    @QueryDefine(condition = QueryCondition.EQ)
    private Long currentType; // 当前考试类型（1系统内部考试，0扫码开放考试）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.GE)
    private Date limitStartTime; // 考试开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.LE)
    private Date limitEndTime; // 考试结束时间

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer valid; // 是否有效（1有效，0无效）

    @TableLogic
    private Integer delFlag; // 是否删除（1删除，0未删除）


    @QueryDefine(condition = QueryCondition.EQ)
    private Integer published; // 是否发布（1已发布，0未发布）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @QueryDefine(condition = QueryCondition.GE)
    private Date publishTime; // 发布时间

    @QueryDefine(condition = QueryCondition.LE)
    private String duration; // 考试时长

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer checkType; // 阅卷方式（0机器，1人工）

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer successScore; // 及格分数

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long paperId; // 试卷模版ID

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer cheat; // 是否防作弊（0关闭，1开启）

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer questionRule; // 题目规则（0固定题目，1随机题目）

    private String classifies; // 随机题目配置依赖分类
}
