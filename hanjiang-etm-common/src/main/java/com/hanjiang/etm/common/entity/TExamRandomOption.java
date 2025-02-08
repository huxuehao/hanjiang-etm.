package com.hanjiang.etm.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hanjiang.etm.common.consts.DBConst;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：随机题目配置
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_EXAM_RANDOM_OPTION)
public class TExamRandomOption implements Serializable {
    // 序列化版本号
    private static final long serialVersionUID = 1L;
    // 考试ID
    private Long examId;
    // 题目类型
    private Integer questionType;
    // 题目数量
    private Integer questionNum;
    // 每题的分数
    private Integer questionScore;
}
