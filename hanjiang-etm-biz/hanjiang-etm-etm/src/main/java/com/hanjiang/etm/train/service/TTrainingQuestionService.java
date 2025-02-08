package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TTrainingQuestion;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：培训-试题
 *
 * @author huxuehao
 **/
public interface TTrainingQuestionService extends IService<TTrainingQuestion> {
    /**
     * 安全批量删除
     * @param questions 试题信息
     * @param trainId   培训ID
     */
    void safeSaveBatch(LinkedList<Object> questions, Long trainId);

    /**
     * 基于培训ID集合进行批量删除
     * @param trainIds 培训ID集合
     */
    void deleteByTrainIds(List<Long> trainIds);

    /**
     * 根据培训ID获取记录
     * @param trainId 培训ID
     */
    List<TTrainingQuestion> getQuestionsByTrainId(Long trainId);

    /**
     * 根据培训ID获取记录
     * @param trainId 培训ID
     */
    List<Object> getObjectByTrainId(Long trainId);
}
