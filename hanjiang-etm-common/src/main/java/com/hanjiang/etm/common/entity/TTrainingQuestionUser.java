package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：培训试题用户表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_TRAIN_QUESTION_USER)
public class TTrainingQuestionUser implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainId; // 培训ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainQuestionId; // 培训-试题ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId; // 培训-试题ID

    private String config; // 试题配置

    private Integer intoVideo; // 是否插入视频中（1插入，0不插入）

    private String intoTime; // 插入视频中的时间

    private Integer sort; // 排序

    private Integer doAlready; // 是否已做（1已做，0未做）

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doTime; // 答题时间

    private Integer score; // 得分

    private Integer success; // 是否正确（1正确，0不正确）

    @TableLogic
    private Integer delFlag; // 是否删除（0未删除，1删除）
}
