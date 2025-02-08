package com.hanjiang.etm.question.factory.template;

import com.alibaba.fastjson2.JSON;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.question.factory.Option;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.CorrectType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 描述：填空试题配置
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class CompletionQuestionConfig extends QuestionConfig {
    // 选项
    private List<Option> option;
    // 答案
    private List<Option> answer;

    @Override
    public boolean defaultAutoCorrect() {
        return false;
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
        for (Option o : this.answer) {
            for (Option option : this.option) {
                if (Func.isEmpty(option.getTitle()) || Func.isEmpty(option.getValue())) {
                    continue;
                }
                if (Func.isEmpty(o.getTitle()) || Func.isEmpty(o.getValue())) {
                    continue;
                }
                if (option.getTitle().equals(o.getTitle().trim()) && option.getValue().equals(o.getValue().trim())) {
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
        } else {
            return 0;
        }
    }
}
