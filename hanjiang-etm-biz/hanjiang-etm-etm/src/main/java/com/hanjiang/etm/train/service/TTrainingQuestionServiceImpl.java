package com.hanjiang.etm.train.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TTrainingQuestion;
import com.hanjiang.etm.train.mapper.TTrainingQuestionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：培训-试题
 *
 * @author huxuehao
 **/
@Service
public class TTrainingQuestionServiceImpl extends ServiceImpl<TTrainingQuestionMapper, TTrainingQuestion> implements TTrainingQuestionService {
    @Override
    @Transactional
    public void safeSaveBatch(LinkedList<Object> questions, Long trainId) {
        // 删除
        QueryWrapper<TTrainingQuestion> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        remove(qw);

        // 保存
        ArrayList<TTrainingQuestion> tTrainingQuestions = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            TTrainingQuestion tTrainingQuestion = new TTrainingQuestion();
            tTrainingQuestion.setTrainId(trainId);
            tTrainingQuestion.setConfig(JSON.toJSONString(questions.get(i)));
            tTrainingQuestion.setIntoVideo(1);
            tTrainingQuestion.setSort(i);
            tTrainingQuestions.add(tTrainingQuestion);
        }
        saveBatch(tTrainingQuestions);
    }

    @Override
    public void deleteByTrainIds(List<Long> trainIds) {
        QueryWrapper<TTrainingQuestion> qw = new QueryWrapper<>();
        qw.in("train_id", trainIds);
        remove(qw);
    }

    @Override
    public List<TTrainingQuestion> getQuestionsByTrainId(Long trainId) {
        QueryWrapper<TTrainingQuestion> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        qw.eq("del_flag", 0);
        qw.orderByAsc("sort");
        return list(qw);
    }
    @Override
    public List<Object> getObjectByTrainId(Long trainId) {
        List<Object> question = new LinkedList<>();
        List<TTrainingQuestion> list = getQuestionsByTrainId(trainId);
        for (int i = 0; i < list.size(); i++) {
            JSONObject questionConfig = JSON.parseObject(list.get(i).getConfig());
            questionConfig.put("rowKey",String.valueOf(i));
            question.add(questionConfig);
        }
        return question;
    }
}
