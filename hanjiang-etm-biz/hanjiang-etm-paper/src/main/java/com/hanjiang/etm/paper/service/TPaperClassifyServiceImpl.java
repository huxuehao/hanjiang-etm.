package com.hanjiang.etm.paper.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TPaperClassifyDto;
import com.hanjiang.etm.common.entity.TPaperClassify;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.paper.mapper.TPaperClassifyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
@Service
public class TPaperClassifyServiceImpl extends ServiceImpl<TPaperClassifyMapper, TPaperClassify> implements TPaperClassifyService {
    @Override
    public List<TPaperClassifyDto> listV2(QueryWrapper<TPaperClassify> queryWrapper) {
        List<TPaperClassifyDto> tQuestionClassifies = this.baseMapper.listV2(DBConst.T_PAPER_CLASSIFY, queryWrapper);
        return TreeUtil.convertTree(tQuestionClassifies);
    }
}
