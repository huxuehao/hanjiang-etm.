package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.mp.annotation.QueryDefine;
import com.hanjiang.etm.common.mp.support.QueryCondition;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：考试批改人
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_EXAM_CORRECTOR)
public class TExamCorrector implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examId; // 考试ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId; // 批改人

    @QueryDefine(condition = QueryCondition.LIKE)
    private String orgName;

    @JsonSerialize(using = ToStringSerializer.class)
    private String orgId;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String userName;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String userCode;
}
