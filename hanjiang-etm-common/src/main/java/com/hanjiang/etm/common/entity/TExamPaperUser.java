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
 * 描述：参考试人员表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_EXAM_PAPER_USER)
public class TExamPaperUser implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    private Long userId; // 用户编号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examId; // 考试ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examPaperId; // 考试试题ID

    private String config; // 试卷配置

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doStartTime; // 开始答题时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doEndTime; // 提交试卷时间

    private Integer score; // 得分

    private Integer success; // 是否合格（1合格，0不合格）

    private Integer corrected; // 是否批改（1已批改，0未批改，-1批改中）

    private String correctUser; // 批改人

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date correctTime; // 批改时间

    @TableLogic
    private Integer delFlag;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer paperDone;
    private Integer sort;
}
