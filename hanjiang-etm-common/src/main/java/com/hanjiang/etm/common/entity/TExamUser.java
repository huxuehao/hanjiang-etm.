package com.hanjiang.etm.common.entity;

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
import java.util.Objects;

/**
 * 描述：考试-用户表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_EXAM_USER)
public class TExamUser implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long examId; // 考试ID

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long userId; // 用户ID

    @QueryDefine(condition = QueryCondition.LIKE)
    private String orgName;

    @JsonSerialize(using = ToStringSerializer.class)
    private String orgId;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String userName;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String userCode;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer paperDone;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer corrected;

    private Integer score;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doTime;

    @QueryDefine(condition = QueryCondition.EQ)
    private String plat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TExamUser tExamUser = (TExamUser) o;
        return Objects.equals(examId, tExamUser.examId) && Objects.equals(userId, tExamUser.userId) && Objects.equals(userCode, tExamUser.userCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examId, userId, userCode);
    }
}
