package com.hanjiang.etm.question.factory.template;

import com.alibaba.fastjson2.JSON;
import com.hanjiang.etm.question.factory.Option;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.CorrectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 描述：单选试题配置
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class SingleQuestionConfig extends QuestionConfig {
    // 选项
    private List<Option> option;
    // 答案
    private String answer;

    @Override
    public boolean defaultAutoCorrect() {
        return true;
    }

    @Override
    public String toJson() {
       return JSON.toJSONString(this);
    }

    @Override
    public QuestionConfig toConfig(String jsonConfig) {
        return JSON.parseObject(jsonConfig, this.getClass());
    }

    @Override
    public Integer doCorrect() {
        for (Option option : this.option) {
            if (option.getSelected() == 1 && option.getTitle().equals(this.answer)) {
                this.setSuccess(CorrectType.SUCCESS.value);
                return this.getSuccess();
            }
        }

        this.setSuccess(CorrectType.ERROR.value);
        return this.getSuccess();
    }

    @Override
    public Integer genScore() {
        if (doCorrect() == CorrectType.SUCCESS.value) {
            return super.getScore();
        } else {
            return 0;
        }
    }
}
