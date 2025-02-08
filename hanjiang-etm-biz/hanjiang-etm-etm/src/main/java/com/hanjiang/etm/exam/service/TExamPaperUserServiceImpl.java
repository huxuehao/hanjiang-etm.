package com.hanjiang.etm.exam.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TExamPaperUser;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.exam.mapper.TExamPaperUserMapper;
import com.hanjiang.etm.common.dto.QuestionConfig;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamPaperUserServiceImpl extends ServiceImpl<TExamPaperUserMapper, TExamPaperUser> implements TExamPaperUserService {

    @Override
    public boolean haveDoneUserByExamId(Long examId) {
        QueryWrapper<TExamPaperUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        qw.eq("paper_done", 1);
        List<TExamPaperUser> list = list(qw);
        return !(list == null || list.isEmpty());
    }

    @Override
    public void deleteByExamId(Long examId) {
        QueryWrapper<TExamPaperUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        remove(qw);
    }

    @Override
    public void deleteOneByIds(Long examId, Long userId) {
        UpdateWrapper<TExamPaperUser> uw = new UpdateWrapper<>();
        uw.eq("user_id", userId);
        uw.eq("exam_id", examId);
        remove(uw);
    }

    @Override
    public LinkedList<Object> getOneUserPaper(Long examId, Long userId) {
        QueryWrapper<TExamPaperUser> questionQw = new QueryWrapper<>();
        questionQw.eq("exam_id", examId);
        questionQw.eq("user_id", userId);
        questionQw.eq("del_flag", 0);
        questionQw.orderByAsc("sort");
        List<TExamPaperUser> list = list(questionQw);
        LinkedList<Object> questions = new LinkedList<>();
        list.forEach(item -> {
            JSONObject questionConfig = JSON.parseObject(item.getConfig());
            questionConfig.put("examPaperUserTableId", String.valueOf(item.getId()));
            questions.add(questionConfig);
        });
        return questions;
    }

    @Override
    public void updateUserPaperWithCorrected(Long examPaperUserTableId, QuestionConfig questionConfig) {
        TExamPaperUser tExamPaperUser = getById(examPaperUserTableId);
        tExamPaperUser.setPaperDone(1);
        tExamPaperUser.setDoEndTime(new Date());
        tExamPaperUser.setScore(questionConfig.getScoreBySuccess());
        tExamPaperUser.setSuccess(questionConfig.getSuccess());
        tExamPaperUser.setCorrected(1);
        tExamPaperUser.setConfig(JSON.toJSONString(questionConfig));
        updateById(tExamPaperUser);
    }

    @Override
    public void updateUserPaperWithCorrectedV2(Long examPaperUserTableId, QuestionConfig questionConfig) {
        TExamPaperUser tExamPaperUser = getById(examPaperUserTableId);
        tExamPaperUser.setPaperDone(1);
        tExamPaperUser.setDoEndTime(new Date());
        tExamPaperUser.setCorrected(questionConfig.getSuccess() != -1 ? 0 : 1);
        tExamPaperUser.setConfig(JSON.toJSONString(questionConfig));
        tExamPaperUser.setCorrectUser(String.valueOf(AuthUtil.getUserId()));
        updateById(tExamPaperUser);
    }
}
