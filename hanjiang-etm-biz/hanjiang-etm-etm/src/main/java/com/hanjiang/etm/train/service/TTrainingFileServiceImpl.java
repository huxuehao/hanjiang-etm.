package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TTrainingFile;
import com.hanjiang.etm.train.mapper.TTrainingFileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：培训-课件
 *
 * @author huxuehao
 **/
@Service
public class TTrainingFileServiceImpl extends ServiceImpl<TTrainingFileMapper, TTrainingFile> implements TTrainingFileService {
    @Override
    @Transactional
    public void safeSaveBatch(LinkedList<TTrainingFile> file, Long trainId) {
        // 删除
        QueryWrapper<TTrainingFile> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        remove(qw);

        // 保存
        for (int i = 0; i < file.size(); i++) {
            file.get(i).setTrainId(trainId);
            file.get(i).setSort(i);
        }
        saveBatch(file);
    }

    @Override
    public void deleteByTrainIds(List<Long> trainIds) {
        QueryWrapper<TTrainingFile> qw = new QueryWrapper<>();
        qw.in("train_id", trainIds);
        remove(qw);
    }

    @Override
    public List<TTrainingFile> getFilesByTrainId(Long trainId) {
        QueryWrapper<TTrainingFile> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        qw.eq("del_flag", 0);
        qw.orderByAsc("sort");
        return list(qw);
    }
}
