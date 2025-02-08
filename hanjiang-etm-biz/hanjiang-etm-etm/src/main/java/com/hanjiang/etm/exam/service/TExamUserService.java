package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TDDInfoDto;
import com.hanjiang.etm.common.entity.TExamUser;

import java.util.Date;
import java.util.List;

/**
 * 描述：考试-用户
 *
 * @author huxuehao
 **/
public interface TExamUserService extends IService<TExamUser> {
    /**
     * 安全批量保存（先删除再报错）
     * @param examId 考试ID
     * @param users  用户
     */
    void safeSaveBatch(Long examId, List<TExamUser> users);

    /**
     * 根据考试ID，获取考试人员信息
     * @param examId 考试ID
     */
    List<TExamUser> selectListByExamId(Long examId);

    /**
     * 重新用户答题记录
     * @param examId 考试ID
     */
    void resetUserAnswer(Long examId);

    /**
     * 根据考试ID删除用户信息
     * @param examId 考试ID
     */
    void deleteByExamId(Long examId);

    /**
     * 根据考试ID和用户ID获取唯一记录
     * @param examId 考试ID
     * @param userId 用户ID
     */
    List<TExamUser> getOneUserInfo(Long examId, Long userId);

    /**
     * 保存APP扫码登录的用户信息
     */
    TExamUser saveUserInfoOfApp(TDDInfoDto tddInfoDto);

    /**
     * 审批后更新用户信息
     * @param tExamUser 考试用户
     * @param score     分数
     * @return 更新时间
     */
    Date updateUserWithCorrected(TExamUser tExamUser, Integer score);
}
