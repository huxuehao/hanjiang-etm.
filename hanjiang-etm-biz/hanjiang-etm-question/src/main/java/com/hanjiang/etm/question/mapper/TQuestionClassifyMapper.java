package com.hanjiang.etm.question.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.dto.TQuestionClassifyDto;
import com.hanjiang.etm.common.entity.TQuestionClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：试题分类
 *
 * @author huxuehao
 **/
@Mapper
public interface TQuestionClassifyMapper extends BaseMapper<TQuestionClassify> {
    List<TQuestionClassifyDto> listV2(@Param("t_question_classify") String tableName, @Param("ew") QueryWrapper<TQuestionClassify> ew);
}
