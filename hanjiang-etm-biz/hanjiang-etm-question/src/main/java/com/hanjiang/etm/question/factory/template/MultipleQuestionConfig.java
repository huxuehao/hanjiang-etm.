package com.hanjiang.etm.question.factory.template;

import com.alibaba.fastjson2.JSON;
import com.hanjiang.etm.question.factory.Option;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.CorrectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 描述：多选试题配置
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class MultipleQuestionConfig extends QuestionConfig {
    // 选项
    private List<Option> option;
    // 答案
    private List<String> answer;

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
        int successNum = 0;
        for (String s : this.answer) {
            for (Option option : this.option) {
                if (option.getSelected() == 1 && option.getTitle().equals(s)) {
                    successNum++;
                    break;
                }
            }
        }
        if (successNum == this.answer.size()) {
            this.setSuccess(CorrectType.SUCCESS.value);
        } else {
            this.setSuccess(CorrectType.ERROR.value);
        }
        return this.getSuccess();
    }

    @Override
    public Integer genScore() {
        if (doCorrect() == CorrectType.SUCCESS.value) {
            return super.getScore();
        } else if (doCorrect() == CorrectType.ERROR.value){
            return 0;
        } else {
            return (int) Math.floor((double) super.getScore() / 2);
        }
    }
}
