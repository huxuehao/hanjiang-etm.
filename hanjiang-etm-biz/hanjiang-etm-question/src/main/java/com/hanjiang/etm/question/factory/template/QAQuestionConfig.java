package com.hanjiang.etm.question.factory.template;

import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import com.alibaba.fastjson2.JSON;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.CorrectType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：解答试题配置
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class QAQuestionConfig extends QuestionConfig {
    // 答案
    private String answer;
    // 关键字
    private Set<String> keyWords;
    // 最小匹配数
    private Integer matchNum;

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
        if (Func.isEmpty(answer)) {
            this.setSuccess(CorrectType.ERROR.value);
            return getSuccess();
        }
        // 将考生答案进行分词
        TokenizerEngine engine = TokenizerUtil.createEngine();
        Set<String> participles = new HashSet<>();
        for (Word word : engine.parse(answer)) {
            participles.add(word.getText());
        }
        // 找出participles核keyWords的交集
        Set<String> contains = participles.stream()
                .filter(keyWords::contains)
                .collect(Collectors.toSet());
        //判断是否正确
        if (contains.size() >= matchNum) {
            this.setSuccess(CorrectType.SUCCESS.value);
        } else {
            this.setSuccess(CorrectType.ERROR.value);
        }
        return getSuccess();
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
