package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TDDInfoDto;
import com.hanjiang.etm.common.dto.TExamDto;
import com.hanjiang.etm.common.entity.TExam;
import com.hanjiang.etm.common.entity.TExamRandomOption;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface TExamService extends IService<TExam> {
    /**
     * 新增
     */
    boolean addV2(TExamDto tExamDto);

    /**
     * 更新
     */
    boolean updateV2(TExamDto tExamDto);

    /**
     * 唯一键值加载
     */
    TExamDto selectByIdV2(Long id);

    /**
     * 系统内部考试发布
     */
    boolean publish(Long id);

    /**
     * 扫码开放考试发布
     */
    boolean publishV2(Long id);

    /**
     * 获取考试配置
     */
    TExamDto selectExamConfig(Long examId, Long userId);

    /**
     * 用户交卷 + 自动审批
     */
    Object examAnswer(TExamDto tExamTemplate);

    /**
     * 用户交卷
     */
    Object examAnswerV2(TExamDto tExamTemplate);

    /**
     * 人工审批
     */
    Object correct(TExamDto tExamTemplate);

    /**
     * 考试取消发布
     * @param id    examId
     * @param type  0扫码开放，1系统内部
     * @param force 是否强制
     */
    Object cancelPublish(Long id,  int type, Integer force);

    /**
     * 检查考试是否过期
     */
    boolean checkExamTimeout(TExam tExam);

    /**
     * 检查考试是否开始
     */
    boolean checkExamNotStart(TExam tExam);

    /**
     * 初始化钉钉扫码的考试
     */
    Object initExamOfDD(TDDInfoDto tddInfoDto);

    /**
     * 批改模式获取试卷配置
     */
    TExamDto examConfigByCorrect(Long examId, Long userId);

    /**
     * 检测随机option是否合法
     * @param options    option配置
     * @param classifies 分类字符串
     */
    void checkRandomOption(List<TExamRandomOption> options, String classifies);
}
