package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 描述：附件表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_ATTACH)
public class TAttach implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long fileId;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String link;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String domain;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String name;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String originalName;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String extension;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String protocol;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String path;
    private Long attachSize;
    private Integer status;
    @QueryDefine(condition = QueryCondition.EQ)
    private String createUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @QueryDefine(condition = QueryCondition.EQ)
    private String updateUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @TableLogic
    private Integer delFlag;
}
