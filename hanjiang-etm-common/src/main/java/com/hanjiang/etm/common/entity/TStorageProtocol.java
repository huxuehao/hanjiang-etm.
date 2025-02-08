package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.mp.annotation.QueryDefine;
import com.hanjiang.etm.common.mp.support.QueryCondition;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：文件存储协议配置
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_STORAGE_PROTOCOL)
public class TStorageProtocol implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    @QueryDefine(condition = QueryCondition.EQ)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @QueryDefine(condition = QueryCondition.LIKE)
    private String name;
    @QueryDefine(condition = QueryCondition.EQ)
    private String protocol;
    private String protocolConfig;
    @QueryDefine(condition = QueryCondition.EQ)
    private String createUser;
    private Date createTime;
    @QueryDefine(condition = QueryCondition.EQ)
    private String updateUser;
    private Date updateTime;
    private String remark;
    @QueryDefine(condition = QueryCondition.EQ)
    private Integer valid;
    @TableLogic
    private Integer delFlag;
}
