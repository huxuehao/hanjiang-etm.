package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TTrainingClassifyDto;
import com.hanjiang.etm.common.entity.TTrainingClassify;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
public interface TTrainingClassifyService extends IService<TTrainingClassify> {
    List<TTrainingClassifyDto> listV2(QueryWrapper<TTrainingClassify> ew);
}
