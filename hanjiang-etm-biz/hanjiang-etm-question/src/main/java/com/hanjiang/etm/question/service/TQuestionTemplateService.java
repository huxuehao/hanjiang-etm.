package com.hanjiang.etm.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TQuestionTemplate;
import com.hanjiang.etm.common.vo.ImportQuestionParserBoxVo;

import java.util.List;

/**
 * 描述：试题模版
 *
 * @author huxuehao
 **/
public interface TQuestionTemplateService extends IService<TQuestionTemplate> {
    /**
     * 获取自定类型的题目资源
     * @param type        题目的类型
     * @param classifies  题目分类的字符串集合
     */
    List<TQuestionTemplate> getQuestionPool(Integer type, String classifies);

    /**
     * 提交导入
     */
    void submitImport(ImportQuestionParserBoxVo importQuestion);
}
