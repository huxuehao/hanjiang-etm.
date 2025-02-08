package com.hanjiang.etm.question.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TQuestionClassifyDto;
import com.hanjiang.etm.common.entity.TQuestionClassify;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.question.mapper.TQuestionClassifyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：试题分类
 *
 * @author huxuehao
 **/
@Service
public class TQuestionClassifyServiceImpl extends ServiceImpl<TQuestionClassifyMapper, TQuestionClassify> implements TQuestionClassifyService {

    @Override
    public List<TQuestionClassifyDto> listV2(QueryWrapper<TQuestionClassify> queryWrapper) {
        List<TQuestionClassifyDto> tQuestionClassifies = this.baseMapper.listV2(DBConst.T_QUESTION_CLASSIFY, queryWrapper);
        return TreeUtil.convertTree(tQuestionClassifies);
    }
}
