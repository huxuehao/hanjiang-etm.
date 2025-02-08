package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TTrainingUser;

import java.util.Date;
import java.util.List;

/**
 * 描述：培训-用户
 *
 * @author huxuehao
 **/
public interface TTrainingUserService extends IService<TTrainingUser> {
    /**
     * 安全批量删除
     * @param user    用户信息
     * @param trainId 培训ID
     */
    void safeSaveBatch(List<TTrainingUser> user, Long trainId);

    /**
     * 基于培训ID集合进行批量删除
     * @param trainIds 培训ID集合
     */
    void deleteByTrainIds(List<Long> trainIds);

    /**
     * 根据培训ID获取记录
     * @param trainId 培训ID
     */
    List<TTrainingUser> getUsersByTrainId(Long trainId);

    /**
     * 重置用户作答
     * @param trainId 培训ID
     */
    void resetUserAnswer(Long trainId);

    /**
     * 获取唯一用户信息
     * @param trainId 培训ID
     * @param userId  用户ID
     */
    List<TTrainingUser> getOneUserInfo(Long trainId, Long userId);

    /**
     * 审批后更新用户信息
     * @param trainingUser 培训用户
     * @param score        分数
     * @return 更新时间
     */
    Date updateUserWithCorrected(TTrainingUser trainingUser, Integer score);
}
