package com.hanjiang.etm.train.service;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TTrainingQuestionUser;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.train.mapper.TTrainingQuestionUserMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 描述：培训-试题-用户
 *
 * @author huxuehao
 **/
@Service
public class TTrainingQuestionUserServiceImpl extends ServiceImpl<TTrainingQuestionUserMapper, TTrainingQuestionUser> implements TTrainingQuestionUserService {
    @Override
    public boolean haveDoneUserByExamId(Long trainId) {
        QueryWrapper<TTrainingQuestionUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        qw.eq("do_already", 1);
        List<TTrainingQuestionUser> list = list(qw);
        return !(list == null || list.isEmpty());
    }

    @Override
    public void deleteByTrainId(Long trainId) {
        QueryWrapper<TTrainingQuestionUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        remove(qw);
    }

    @Override
    public List<TTrainingQuestionUser> getOnePaper(Long trainId, Long userId) {
        QueryWrapper<TTrainingQuestionUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        qw.eq("user_id", userId);
        qw.eq("del_flag", 0);
        qw.orderByAsc("sort");
        return list(qw);
    }

    @Override
    public void updateUserPaperWithCorrected(Long trainQuestionUserTableId, QuestionConfig questionConfig) {
        TTrainingQuestionUser tTrainingQuestionUser = getById(trainQuestionUserTableId);
        tTrainingQuestionUser.setDoAlready(1);
        tTrainingQuestionUser.setDoTime(new Date());
        tTrainingQuestionUser.setScore(questionConfig.getScoreBySuccess());
        tTrainingQuestionUser.setSuccess(questionConfig.getSuccess());
        tTrainingQuestionUser.setConfig(JSON.toJSONString(questionConfig));
        updateById(tTrainingQuestionUser);
    }
}
