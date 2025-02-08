package com.hanjiang.etm.exam.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TExamPaper;
import com.hanjiang.etm.exam.mapper.TExamPaperMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamPaperServiceImpl extends ServiceImpl<TExamPaperMapper, TExamPaper> implements TExamPaperService {

    @Override
    @Transactional
    public void safeSaveBatch(Long examId, LinkedList<Object> questions) {
        // 删除
        QueryWrapper<TExamPaper> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        remove(qw);

        // 批量保存
        if (questions != null && !questions.isEmpty()) {
            List<TExamPaper> tExamPapers = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                TExamPaper tExamPaper = new TExamPaper();
                tExamPaper.setExamId(examId);
                tExamPaper.setConfig(JSON.toJSONString(questions.get(i)));
                tExamPaper.setSort(i);
                tExamPapers.add(tExamPaper);
            }
            saveBatch(tExamPapers);
        }
    }

    @Override
    public List<TExamPaper> selectListByExamId(Long examId) {
        QueryWrapper<TExamPaper> tExamPaperQw = new QueryWrapper<>();
        tExamPaperQw.eq("exam_id", examId);
        tExamPaperQw.orderByAsc("sort");
        return list(tExamPaperQw);
    }

    @Override
    public LinkedList<Object> selectObjectListByExamId(Long examId) {
        List<TExamPaper> papers = selectListByExamId(examId);
        LinkedList<Object> questions = new LinkedList<>();
        for (int i = 0; i < papers.size(); i++) {
            JSONObject questionConfig = JSON.parseObject(papers.get(i).getConfig());
            questionConfig.put("rowKey",String.valueOf(i));
            questions.add(questionConfig);
        }
        return questions;
    }
}
