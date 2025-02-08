package com.hanjiang.etm.question.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TQuestionTemplate;
import com.hanjiang.etm.common.vo.ImportQuestionParserBoxVo;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.QuestionConfigFactory;
import com.hanjiang.etm.question.mapper.TQuestionTemplateMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：试题模版
 *
 * @author huxuehao
 **/
@Service
public class TQuestionTemplateServiceImpl extends ServiceImpl<TQuestionTemplateMapper, TQuestionTemplate> implements TQuestionTemplateService {

    @Override
    public List<TQuestionTemplate> getQuestionPool(Integer type, String classifies) {
        QueryWrapper<TQuestionTemplate> questionTemplateQW = new QueryWrapper<>();
        questionTemplateQW.eq("type", type);
        if (classifies != null && classifies.length() > 0) {
            String[] split = classifies.split(",");
            List<String> classifyIds = Arrays.asList(split);
            questionTemplateQW.in("classify_id", classifyIds);
        }
        return list(questionTemplateQW);
    }

    @Override
    @Transactional
    public void submitImport(ImportQuestionParserBoxVo importQuestion) {
        // 导入试题
        List<Object> configs = importQuestion.getSingle().getConfigs();
        configs.addAll(importQuestion.getMultiple().getConfigs());
        configs.addAll(importQuestion.getJudge().getConfigs());
        configs.addAll(importQuestion.getCompletion().getConfigs());
        configs.addAll(importQuestion.getQa().getConfigs());
        for (Object config : configs) {
            TQuestionTemplate questionTemplate = new TQuestionTemplate();
            String questionJsonString = JSON.toJSONString(config);
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(questionJsonString);
            BeanUtils.copyProperties(questionConfig, questionTemplate);
            questionTemplate.setConfig(questionJsonString);
            questionTemplate.setExcel(1);
            save(questionTemplate);
        }
    }
}
