package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TExamRandomOption;
import com.hanjiang.etm.exam.mapper.TExamRandomOptionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamRandomOptionServiceImpl extends ServiceImpl<TExamRandomOptionMapper, TExamRandomOption> implements TExamRandomOptionService {
    @Override
    @Transactional
    public void safeSaveBatch(Long examId, List<TExamRandomOption> randomOptions) {
        // 删除
        QueryWrapper<TExamRandomOption> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        remove(qw);

        // 批量保存
        if (randomOptions != null && !randomOptions.isEmpty()) {
            randomOptions.forEach(item -> item.setExamId(examId));
            saveBatch(randomOptions);
        }
    }

    @Override
    public List<TExamRandomOption> selectListByExamId(Long examId) {
        QueryWrapper<TExamRandomOption> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        return list(qw);
    }
}
