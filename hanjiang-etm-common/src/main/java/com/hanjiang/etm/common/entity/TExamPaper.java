package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：考试-试卷表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_EXAM_PAPER)
public class TExamPaper implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId; // 试卷模版ID

    private String config; // 试卷配置（基于试卷模版）

    @TableLogic
    private Integer delFlag; // 是否删除（1删除，0未删除）

    @JsonSerialize(using = ToStringSerializer.class)
    private Long examId; // 考试ID


    private Integer sort; // 试题排序
}
