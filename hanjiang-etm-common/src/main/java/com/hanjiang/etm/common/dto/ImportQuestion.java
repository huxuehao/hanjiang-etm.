package com.hanjiang.etm.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：导入试题对象
 *
 * @author huxuehao
 **/
public class ImportQuestion {
    List<Long> classifyIds = new ArrayList<>();
    List<? extends QuestionConfig> configs = new ArrayList<>();

    public List<Long> getClassifyIds() {
        return classifyIds;
    }

    public ImportQuestion setClassifyIds(List<Long> classifyIds) {
        this.classifyIds = classifyIds;
        return this;
    }

    public List<? extends QuestionConfig> getConfigs() {
        return configs;
    }

    public ImportQuestion setConfigs(List<? extends QuestionConfig> configs) {
        this.configs = configs;
        return this;
    }
}
