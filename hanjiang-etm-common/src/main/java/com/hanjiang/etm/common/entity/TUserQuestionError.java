package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：用户错题集表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_USER_QUESTION_ERROR)
public class TUserQuestionError implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    public String userCode; // 用户编号

    @JsonSerialize(using = ToStringSerializer.class)
    public Long examId; // 考试ID

    @JsonSerialize(using = ToStringSerializer.class)
    public Long examPaperId; // 考试试卷ID

    public String questionConfig; // 试题配置
}
