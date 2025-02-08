package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
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
@TableName(DBConst.T_TASK)
public class TTask implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examTranId;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long classifyId;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String classifyName; // 分类名称

    @QueryDefine(condition = QueryCondition.LIKE)
    private String showName; // 考试培训名称

    private String userCode; // 用户编码

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime; // 发布时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.GE)
    private Date limitStartTime; // 考试培训开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.LE)
    private Date limitEndTime; // 考试培训结束时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.GE)
    private Date doStartTime; // 考试培训开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @QueryDefine(condition = QueryCondition.LE)
    private Date doEndTime; // 考试培训结束时间

    @JsonSerialize(nullsUsing = NullSerializer.class)
    private Integer score; // 分数

    private Integer showType; // 区分考试培训类型

    private Integer valid; // 是否有效（1有效，0无效）

    private Integer showStatus; // 状态展示
}
