package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TExamPaperUser;
import com.hanjiang.etm.common.dto.QuestionConfig;

import java.util.LinkedList;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface TExamPaperUserService extends IService<TExamPaperUser> {
    /**
     * 检测是否有已完成的用户
     * @param examId 考试ID
     */
    boolean haveDoneUserByExamId(Long examId);

    /**
     * 根据考试ID删除用户信息
     * @param examId 考试ID
     */
    void deleteByExamId(Long examId);

    /**
     * 根据考试ID删除用户信息
     * @param examId 考试ID
     * @param userId 用户ID
     */
    void deleteOneByIds(Long examId, Long userId);

    /**
     * 根据考试ID和用户ID获取唯一试卷
     * @param examId 考试ID
     * @param userId 用户ID
     */
    LinkedList<Object> getOneUserPaper(Long examId, Long userId);

    /**
     * 审批后更新用户试卷表信息
     * @param examPaperUserTableId 用户试卷表ID
     * @param questionConfig       试题配置
     */
    void updateUserPaperWithCorrected(Long examPaperUserTableId, QuestionConfig questionConfig);

    /**
     * 手动审批，但是部分题型支持自动审批，后更新用户试卷表信息
     * @param examPaperUserTableId 用户试卷表ID
     * @param questionConfig       试题配置
     */
    void updateUserPaperWithCorrectedV2(Long examPaperUserTableId, QuestionConfig questionConfig);

}
