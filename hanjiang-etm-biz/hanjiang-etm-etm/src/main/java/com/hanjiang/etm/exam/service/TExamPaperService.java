package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TExamPaper;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface TExamPaperService extends IService<TExamPaper> {
    /**
     * 安全批量保存（先删除再报错）
     * @param examId     考试ID
     * @param questions  试题集合
     */
    void safeSaveBatch(Long examId, LinkedList<Object> questions);


    /**
     * 根据考试ID，获取考试试卷
     * @param examId 考试ID
     */
    List<TExamPaper> selectListByExamId(Long examId);

    /**
     * 根据考试ID，获取考试试卷
     * @param examId 考试ID
     */
    LinkedList<Object> selectObjectListByExamId(Long examId);
}
