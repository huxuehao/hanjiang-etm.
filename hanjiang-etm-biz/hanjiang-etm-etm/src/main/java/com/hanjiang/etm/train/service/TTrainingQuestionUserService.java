package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TTrainingQuestionUser;
import com.hanjiang.etm.common.dto.QuestionConfig;

import java.util.List;

/**
 * 描述：培训-试题-用户
 *
 * @author huxuehao
 **/
public interface TTrainingQuestionUserService extends IService<TTrainingQuestionUser> {
    /**
     * 检测是否有已完成的用户
     * @param trainId 培训ID
     */
    boolean haveDoneUserByExamId(Long trainId);

    /**
     * 根据培训ID删除记录
     * @param trainId 培训ID
     */
    void deleteByTrainId(Long trainId);

    /**
     * 获取唯一试卷信息
     * @param trainId 培训ID
     * @param userId  用户ID
     */
    List<TTrainingQuestionUser> getOnePaper(Long trainId, Long userId);

    /**
     * 审批后更新用户试卷表信息
     * @param trainQuestionUserTableId 用户试卷表ID
     * @param questionConfig           试题配置
     */
    void updateUserPaperWithCorrected(Long trainQuestionUserTableId, QuestionConfig questionConfig);
}
