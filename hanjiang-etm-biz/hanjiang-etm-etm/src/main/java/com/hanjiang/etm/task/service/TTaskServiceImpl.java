package com.hanjiang.etm.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TExamCorrector;
import com.hanjiang.etm.common.entity.TTask;
import com.hanjiang.etm.common.function.AccessFunction;
import com.hanjiang.etm.common.function.QueryWrapperFunction;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.exam.service.TExamCorrectorService;
import com.hanjiang.etm.task.mapper.TTaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TTaskServiceImpl extends ServiceImpl<TTaskMapper, TTask> implements TTaskService {
    private final TTaskMapper tTaskMapper;
    private final TExamCorrectorService tExamCorrectorService;

    public TTaskServiceImpl(TTaskMapper tTaskMapper, TExamCorrectorService tExamCorrectorService) {
        this.tTaskMapper = tTaskMapper;
        this.tExamCorrectorService = tExamCorrectorService;
    }

    @Override
    public List<TTask> list(TTask tUserShow, String activeType) {
        QueryWrapper<TTask> queryWrapper = MP.getQueryWrapper(tUserShow);
        // 待培训
        if ("ntrain".equals(activeType)) {
            queryWrapper.eq("show_type", 1);
            queryWrapper.eq("show_status", 0);
        }
        // 已培训
        else if ("ytrain".equals(activeType)) {
            queryWrapper.eq("show_type", 1);
            queryWrapper.eq("show_status", 1);

        }
        // 待考试
        else if ("nexam".equals(activeType)) {
            queryWrapper.eq("show_type", 0);
            queryWrapper.eq("show_status", 0);
        }
        // 已考试 后续考虑手工批阅的
        else if ("yexam".equals(activeType)) {
            queryWrapper.eq("show_type", 0);
            queryWrapper.in("show_status", 1, 2);
        } else {
            throw new RuntimeException("acctiveType匹配失败");
        }
        queryWrapper.eq("user_code", AuthUtil.getUserId());
        queryWrapper.orderByDesc("publish_time");

        return tTaskMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<TTask> page(TTask tUserShow, PageParams pageParams, String activeType, Integer status) {
        // 参数校验
        if (activeType == null || activeType.isEmpty()) {
            throw new IllegalArgumentException("activeType 不能为空");
        }
        if (tUserShow == null) {
            tUserShow = new TTask(); // 防止空指针
        }

        QueryWrapper<TTask> queryWrapper = MP.getQueryWrapper(tUserShow);

        switch (activeType) {
            case "exam":
                queryWrapper.eq("show_type", 0).ne("show_status", 2);
                break;
            case "train":
                queryWrapper.eq("show_type", 1).ne("show_status", 2);
                break;
            case "approval":
                queryWrapper.eq("show_type", 0).eq("show_status", 2);
                List<TExamCorrector> users = tExamCorrectorService.list(new QueryWrapper<TExamCorrector>().eq("user_id", AuthUtil.getUserId()));
                if (!users.isEmpty()) {
                    List<Long> examIds = users.stream().map(TExamCorrector::getExamId).collect(Collectors.toList());
                    queryWrapper.in("exam_tran_id", examIds);
                } else {
                    queryWrapper.eq("id", "1234321");
                }
                break;
            default:
                throw new RuntimeException("activeType 匹配失败");
        }

        LocalDateTime now = LocalDateTime.now();

        if (status != null) {
            switch (status) {
                case 0: // 待完成
                    queryWrapper.gt("limit_end_time", now)
                            .lt("limit_start_time", now)
                            .eq("show_status", 0);
                    break;
                case 1: // 未开始
                    queryWrapper.gt("limit_start_time", now);
                    break;
                case 2: // 已过期
                    queryWrapper.lt("limit_end_time", now)
                            .eq("show_status", 0);
                    break;
                case 3: // 已完成
                    queryWrapper.eq("show_status", 1);
                    break;
                default:
                    throw new IllegalArgumentException("未知的状态值");
            }
        }

        // 设置 user_code 条件 审批模式查看不了自己试卷
        if ("approval".equals(activeType)) {
            queryWrapper.ne("user_code", AuthUtil.getUserId()); // 不等于
        } else {
            queryWrapper.eq("user_code", AuthUtil.getUserId()); // 等于
        }

        // 自定义排序规则
        queryWrapper.last(
                "ORDER BY CASE " +
                        "WHEN limit_end_time > NOW() AND limit_start_time < NOW() AND show_status = 0 THEN 1 " +
                        "WHEN limit_start_time > NOW() THEN 2 " +
                        "WHEN show_status = 1 THEN 3 " +
                        "WHEN limit_end_time < NOW() THEN 4 " +
                        "ELSE 5 END, publish_time DESC"
        );

        return tTaskMapper.selectPage(MP.getPage(pageParams), queryWrapper);
    }

    @Override
    @Transactional
    public void accessByPublished(List<Long> userIds, int type, AccessFunction access) {
        ArrayList<TTask> userShows = new ArrayList<>();
        for (int i = 1; i < userIds.size() + 1; i++) {
            TTask userShow = new TTask();
            access.setAttribute(userShow);
            userShow.setUserCode(String.valueOf(userIds.get(i - 1)));
            userShow.setShowType(type);
            userShow.setShowStatus(0);
            userShow.setValid(1);
            userShows.add(userShow);
            // 每100条入一次库
            if (i % 100 == 0) {
                saveBatch(userShows);
                userShows = new ArrayList<>();
            } else if (i == userIds.size()) {
                saveBatch(userShows);
            }
        }
    }

    @Override
    public void deleteByMixinsId(Long mixinsId) {
        QueryWrapper<TTask> qw = new QueryWrapper<>();
        qw.eq("exam_tran_id", mixinsId);
        remove(qw);
    }

    @Override
    public void updateDoStartTime(Long mixinsId, Long userId, int type) {
        QueryWrapper<TTask> qw = new QueryWrapper<>();
        qw.eq("exam_tran_id", mixinsId);
        qw.eq("user_code", String.valueOf(userId));
        qw.eq("show_type", type);
        List<TTask> list1 = list(qw);
        for (TTask tUserShow : list1) {
            tUserShow.setDoStartTime(new Date());
            updateById(tUserShow);
        }
    }

    @Override
    public void updateByCorrected(Date date, Integer score, QueryWrapperFunction queryWrapperFunction) {
        QueryWrapper<TTask> qw = new QueryWrapper<>();
        // 调用方自己设置查询条件
        queryWrapperFunction.setCondition(qw);
        List<TTask> userShowList = list(qw);
        for (TTask tUserShow : userShowList) {
            if (score == null) {
                tUserShow.setShowStatus(2);
            } else {
                tUserShow.setScore(score);
                tUserShow.setShowStatus(1);
            }
            tUserShow.setDoEndTime(date);
            updateById(tUserShow);
        }
    }

}
