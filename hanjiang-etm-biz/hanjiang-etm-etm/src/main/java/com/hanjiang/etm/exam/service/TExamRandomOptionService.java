package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TExamRandomOption;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface TExamRandomOptionService extends IService<TExamRandomOption> {
    /**
     * 安全批量保存（先删除再报错）
     * @param examId         考试ID
     * @param randomOptions  随机配置
     */
    void safeSaveBatch(Long examId, List<TExamRandomOption> randomOptions);

    /**
     * 根据考试ID，获取考试随机配置
     * @param examId 考试ID
     */
    List<TExamRandomOption> selectListByExamId(Long examId);
}
