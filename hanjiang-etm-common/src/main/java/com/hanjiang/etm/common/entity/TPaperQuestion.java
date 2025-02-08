package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：试卷-试题表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_PAPER_QUESTION)
public class TPaperQuestion implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId; // 所属试卷ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId; // 试题模版ID

    private String config; // 试题配置（基于试题模版）

    private Integer sort; // 排序

    private Integer delFlag; // 是否删除（0未删除，1删除）
}
