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
 * 描述：培训试题表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_TRAIN_QUESTION)
public class TTrainingQuestion implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainId; // 培训ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long questionId; // 试题ID

    private String config; // 试题配置

    private Integer intoVideo; // 是否插入视频中（1插入，0不插入）

    private String intoTime; // 插入视频中的时间

    private Integer sort; // 排序

    @TableLogic
    private Integer delFlag; // 是否删除（0未删除，1删除）
}
