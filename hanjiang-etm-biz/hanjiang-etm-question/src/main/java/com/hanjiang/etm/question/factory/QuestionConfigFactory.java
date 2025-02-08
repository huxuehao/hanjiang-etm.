package com.hanjiang.etm.question.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.QuestionType;

/**
 * 描述：试题配置解析工厂
 * 将任何类型的试题JSON配置转成QuestionConfig对象
 *
 * @author huxuehao
 **/
public class QuestionConfigFactory {

    /**
     * 将任何类型的Question字符串JSON构造成QuestionConfig对象
     *
     * @param jsonConfig JSON 字符串
     */
    public static QuestionConfig buildInstance(String jsonConfig) {
        JSONObject config = JSON.parseObject(jsonConfig);
        for (QuestionType value : QuestionType.values()) {
            if (value.value == config.getInteger("type")) {
                try {
                    return value.clazz.newInstance().toConfig(jsonConfig);
                } catch (Exception e) {
                    throw new RuntimeException("实例化QuestionConfig失败");
                }
            }
        }
        throw new RuntimeException("试题配置匹配失败");
    }
}
