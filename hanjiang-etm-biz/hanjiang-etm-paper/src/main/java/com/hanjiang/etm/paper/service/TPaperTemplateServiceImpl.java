package com.hanjiang.etm.paper.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.dto.TPaperTemplateDto;
import com.hanjiang.etm.common.entity.TPaperQuestion;
import com.hanjiang.etm.common.entity.TPaperTemplate;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.paper.mapper.TPaperTemplateMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：试卷
 *
 * @author huxuehao
 **/
@Service
public class TPaperTemplateServiceImpl extends ServiceImpl<TPaperTemplateMapper, TPaperTemplate> implements TPaperTemplateService {

    private final TPaperQuestionService paperQuestionService;

    public TPaperTemplateServiceImpl(TPaperQuestionService paperQuestionService) {
        this.paperQuestionService = paperQuestionService;
    }

    /**
     * 保存
     */
    @Override
    @Transactional
    public boolean saveV2(TPaperTemplateDto paperTemplateDto) {
        //存在性检查
        LinkedList<Object> questions = paperTemplateDto.getQuestion();
        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("试卷试题不可为空");
        }

        // 处理并保存试卷模版主表
        paperTemplateDto.setCreateTime(new Date());
        paperTemplateDto.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        TPaperTemplate paperTemplate = new TPaperTemplate();
        BeanUtils.copyProperties(paperTemplateDto, paperTemplate);
        save(paperTemplate);

        // 处理并保存试卷模版子表
        saveBatchPaperQuestion(questions, paperTemplate.getId());

        return true;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional
    public boolean removeBatchByIdsV2(List<Long> ids) {
        removeBatchByIds(ids);
        //deletePaperQuestionByPaperIds(ids);
        return true;
    }

    /**
     * 批量删除字表数据
     */
    public void deletePaperQuestionByPaperIds(List<Long> ids) {
        QueryWrapper<TPaperQuestion> qw = new QueryWrapper<>();
        qw.in("paper_id", ids);
        paperQuestionService.remove(qw);
    }

    /**
     * 更新
     */
    @Override
    @Transactional
    public boolean updateByIdV2(TPaperTemplateDto paperTemplateDto) {
        // 存在性检查
        LinkedList<Object> questions = paperTemplateDto.getQuestion();
        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("试卷试题不可为空");
        }

        // 处理并保存试卷模版主表
        TPaperTemplate paperTemplate = new TPaperTemplate();
        BeanUtils.copyProperties(paperTemplateDto, paperTemplate);
        updateById(paperTemplate);

        // 删除字表数据
        deletePaperQuestionByPaperIds(new ArrayList<Long>(){{
            add(paperTemplateDto.getId());
        }});

        // 保存字表数据
        saveBatchPaperQuestion(questions, paperTemplate.getId());

        return true;
    }

    @Override
    public TPaperTemplateDto getByIdV2(Long id) {
        //获取主表数据
        TPaperTemplate paperTemplate = getById(id);
        if (paperTemplate == null) {
            throw new RuntimeException("当前试卷已被删除，请重新选择");
        }
        TPaperTemplateDto tPaperTemplateDto = new TPaperTemplateDto();
        BeanUtils.copyProperties(paperTemplate, tPaperTemplateDto);

        // 获取子表数据
        QueryWrapper<TPaperQuestion> qw = new QueryWrapper<>();
        qw.eq("paper_id", paperTemplate.getId());
        qw.eq("del_flag", 0);
        qw.orderByAsc("sort");
        //填充子表对象
        List<TPaperQuestion> list = paperQuestionService.list(qw);
        LinkedList<Object> question = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject questionConfig = JSON.parseObject(list.get(i).getConfig());
            questionConfig.put("rowKey",String.valueOf(i));
            question.add(questionConfig);
        }
        tPaperTemplateDto.setQuestion(question);

        return tPaperTemplateDto;
    }

    /**
     * 批量保存字表数据
     */
    public void saveBatchPaperQuestion(List<Object> questions, Long paperId) {
        LinkedList<TPaperQuestion> tPaperQuestions = new LinkedList<>();
        for (int i = 0; i < questions.size(); i++) {
            TPaperQuestion tPaperQuestion = new TPaperQuestion();
            tPaperQuestion.setPaperId(paperId);
            tPaperQuestion.setConfig(JSON.toJSONString(questions.get(i)));
            tPaperQuestion.setSort(i);
            tPaperQuestions.add(tPaperQuestion);
        }
        paperQuestionService.saveBatch(tPaperQuestions);
    }
}
