package com.hanjiang.etm.statistics.service;

/**
 * 描述：统计分析
 *
 * @author huxuehao
 **/
public interface StatisticsService {
    /**
     * 用户活跃次数统计
     */
    Object userActive();

    /**
     * 题目类型统计
     */
    Object questionType();

    /**
     * 题目难度统计
     */
    Object questionLevel();

    /**
     * 考试培训状态统计
     */
    Object examTrainStatus();
}
