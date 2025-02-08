package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TExamClassifyDto;
import com.hanjiang.etm.common.entity.TExamClassify;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.exam.mapper.TExamClassifyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamClassifyServiceImpl extends ServiceImpl<TExamClassifyMapper, TExamClassify> implements TExamClassifyService {
    @Override
    public List<TExamClassifyDto> examTree(QueryWrapper<TExamClassify> queryWrapper) {
        List<TExamClassifyDto> tExamClassifyDtos = this.baseMapper.examTree(DBConst.T_EXAM_CALSSIFY,queryWrapper);

        return TreeUtil.convertTree(tExamClassifyDtos);
    }
}
