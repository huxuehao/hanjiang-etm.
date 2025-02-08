package com.hanjiang.etm.exam.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.dto.TExamClassifyDto;
import com.hanjiang.etm.common.entity.TExamClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Mapper
public interface TExamClassifyMapper extends BaseMapper<TExamClassify> {
    List<TExamClassifyDto> examTree(@Param("t_exam_classify") String tableName, @Param("ew") QueryWrapper<TExamClassify> ew);
}
