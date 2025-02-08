package com.hanjiang.etm.question.factory.enums;

import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.template.*;

/**
 * 描述：试题类型
 *
 * @author huxuehao
 **/
public enum QuestionType {
    SINGLE(1, "单选", SingleQuestionConfig.class),
    MULTIPLE(2, "多选", MultipleQuestionConfig.class),
    JUDGE(3, "判断", JudgeQuestionConfig.class),
    COMPLETION(4, "填空", CompletionQuestionConfig.class),
    QA(5, "解答", QAQuestionConfig.class);
    public final int value;
    public final String desc;
    public final Class<? extends QuestionConfig> clazz;

    QuestionType(int value, String desc, Class<? extends QuestionConfig> clazz) {
        this.value = value;
        this.desc = desc;
        this.clazz = clazz;
    }
}
