package com.hanjiang.etm.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 描述：考试培训状态统计
 *
 * @author huxuehao
 **/
@Data
public class ExamTrainInfoVo {
    List<ExamTrainInfo> exam;
    List<ExamTrainInfo> train;
}
