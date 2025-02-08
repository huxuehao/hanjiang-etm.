package com.hanjiang.etm.statistics.controller;

import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.statistics.service.StatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：统计分析
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/statistics")
@MenuTag(code = "home")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PreAuthorize("@ps.hasPermission('get::statistics:user-active')")
    @GetMapping(value = "/user-active", name = "用户活跃次数统计")
    public R<?> userActive() {
        return R.data(statisticsService.userActive());
    }

    @PreAuthorize("@ps.hasPermission('get::statistics:question-type')")
    @GetMapping(value = "/question-type", name = "题目类型统计")
    public R<?> questionType() {
        return R.data(statisticsService.questionType());
    }

    @PreAuthorize("@ps.hasPermission('get::statistics:question-level')")
    @GetMapping(value = "/question-level", name = "题目难度统计")
    public R<?> questionLevel() {
        return R.data(statisticsService.questionLevel());
    }

    @PreAuthorize("@ps.hasPermission('get::statistics:exam-train-status')")
    @GetMapping(value = "/exam-train-status", name = "考试培训状态统计")
    public R<?> examTrainStatus() {
        return R.data(statisticsService.examTrainStatus());
    }
}
