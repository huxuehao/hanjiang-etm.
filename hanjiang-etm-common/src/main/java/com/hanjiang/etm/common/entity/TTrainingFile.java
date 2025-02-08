package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 描述：培训课件表
 *
 * @author huxuehao
 */
@Data
@TableName(DBConst.T_TRAIN_FILE)
public class TTrainingFile implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainId; // 培训ID

    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId; // 文件ID

    private String fileExtension; // 文件后缀

    private String fileName; // 文件名称

    private String fileDesc; // 文件描述

    private Integer fileType; // 文件类型：1 视频，2 PDF

    private String fileSize; // 文件大下

    private String fileDuration; // 时长

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime; // 上传时间

    private Integer sort; // 排序

    private Integer delFlag; // 是否删除（0未删除，1删除）
}
