package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TExamCorrector;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface TExamCorrectorService extends IService<TExamCorrector> {
    /**
     * 安全批量保存（先删除再报错）
     * @param examId     考试ID
     * @param correctors 审批人
     */
    void safeSaveBatch(Long examId, List<TExamCorrector> correctors);

    /**
     * 根据考试ID，获取审批人员
     * @param examId 考试ID
     */
    List<TExamCorrector> selectListByExamId(Long examId);

    /**
     * 根据考试ID和用户ID获取唯一记录
     * @param examId 考试ID
     * @param userId 用户ID
     */
    List<TExamCorrector> getOneCorrectorInfo(Long examId, Long userId);
}
