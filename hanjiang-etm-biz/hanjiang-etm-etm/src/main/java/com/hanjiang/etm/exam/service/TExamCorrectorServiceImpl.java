package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TExamCorrector;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.exam.mapper.TExamCorrectorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamCorrectorServiceImpl extends ServiceImpl<TExamCorrectorMapper, TExamCorrector> implements TExamCorrectorService {
    @Override
    @Transactional
    public void safeSaveBatch(Long examId, List<TExamCorrector> correctors) {
        // 删除
        QueryWrapper<TExamCorrector> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        remove(qw);

        // 批量保存
        if (correctors != null && !correctors.isEmpty()) {
            correctors.forEach(item -> item.setExamId(examId));
            saveBatch(correctors);
        }
    }

    @Override
    public List<TExamCorrector> selectListByExamId(Long examId) {
        QueryWrapper<TExamCorrector> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        return list(qw);
    }

    @Override
    public List<TExamCorrector> getOneCorrectorInfo(Long examId, Long userId) {
        QueryWrapper<TExamCorrector> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        qw.eq("user_id", AuthUtil.getUserId());
        return list(qw);
    }
}
