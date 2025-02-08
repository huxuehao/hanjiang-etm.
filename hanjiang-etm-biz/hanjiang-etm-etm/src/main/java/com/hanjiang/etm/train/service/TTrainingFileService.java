package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TTrainingFile;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：培训-课件
 *
 * @author huxuehao
 **/
public interface TTrainingFileService extends IService<TTrainingFile> {
    /**
     * 安全批量删除
     * @param file    附件信息
     * @param trainId 培训ID
     */
    void safeSaveBatch(LinkedList<TTrainingFile> file, Long trainId);

    /**
     * 基于培训ID集合进行批量删除
     * @param trainIds 培训ID集合
     */
    void deleteByTrainIds(List<Long> trainIds);

    /**
     * 根据培训ID获取记录
     * @param trainId 培训ID
     */
    List<TTrainingFile> getFilesByTrainId(Long trainId);
}
