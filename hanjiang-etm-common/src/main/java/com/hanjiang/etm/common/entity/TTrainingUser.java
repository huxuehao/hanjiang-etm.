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

/**
 * 描述：培训课件表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_TRAIN_USER)
public class TTrainingUser implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long trainId; // 培训ID

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long userId; // 文件ID

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

    private Integer score;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doTime;

    @QueryDefine(condition = QueryCondition.EQ)
    private String plat;
}
