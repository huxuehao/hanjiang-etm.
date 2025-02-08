package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 描述：附件表分片记录表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_ATTACH_CHUNK)
public class TAttachChunk implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String chunkHash;
    @QueryDefine(condition = QueryCondition.EQ)
    private Integer chunkIndex;
    @QueryDefine(condition = QueryCondition.EQ)
    private Integer chunkTotals;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String fileKey;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String fileName;
    @QueryDefine(condition = QueryCondition.EQ)
    private Integer fileTotalSize;

    private String createUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
