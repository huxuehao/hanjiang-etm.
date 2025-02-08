package com.hanjiang.etm.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Data
@Getter
@Setter
public class AnalysisDto {

    private String  examTotal; // 考试总数

    private String  examActive; //活跃考试

    private String  trainTotal; // 培训总数

    private String  trainActive; // 活跃培训

    private String  questionTotal; // 题目总数

    private String  paperTotal;  // 试卷总数

    private List<AnalysisActiveDto> aclist;//用户活跃

    private List<Map<String,Object>> qulist;// 题目类型占比

    private List<Map<String,Object>> kslist; // 考试完成情况

    private List<Map<String,Object>> pxlist; // 培训完成情况

    private List<int[]>  sitlist;//  题目难度分布情况

}
