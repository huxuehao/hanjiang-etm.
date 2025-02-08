package com.hanjiang.etm.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：导入试题对象
 *
 * @author huxuehao
 **/
@Data
public class ImportQuestionVo {
    List<Long> classifyIds = new ArrayList<>();
    List<Object> configs = new ArrayList<>();
    public ImportQuestionVo setClassifyIds(List<Long> classifyIds) {
        this.classifyIds = classifyIds;
        return this;
    }

    public ImportQuestionVo setConfigs(List<Object> configs) {
        this.configs = configs;
        return this;
    }
}
