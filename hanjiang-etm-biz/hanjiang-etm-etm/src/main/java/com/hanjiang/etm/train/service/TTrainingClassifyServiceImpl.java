package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TTrainingClassifyDto;
import com.hanjiang.etm.common.entity.TTrainingClassify;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.train.mapper.TTrainingClassifyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
@Service
public class TTrainingClassifyServiceImpl extends ServiceImpl<TTrainingClassifyMapper, TTrainingClassify> implements TTrainingClassifyService {
    @Override
    public List<TTrainingClassifyDto> listV2(QueryWrapper<TTrainingClassify> queryWrapper) {
        List<TTrainingClassifyDto> tNoticeClassifies = this.baseMapper.listV2(DBConst.T_TRAIN_CLASSIFY, queryWrapper);
        return TreeUtil.convertTree(tNoticeClassifies);
    }
}
