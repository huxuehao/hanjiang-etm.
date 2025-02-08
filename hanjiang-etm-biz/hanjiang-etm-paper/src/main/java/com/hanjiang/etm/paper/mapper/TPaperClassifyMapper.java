package com.hanjiang.etm.paper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.dto.TPaperClassifyDto;
import com.hanjiang.etm.common.entity.TPaperClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：试卷分类
 *
 * @author huxuehao
 **/
@Mapper
public interface TPaperClassifyMapper extends BaseMapper<TPaperClassify> {
    List<TPaperClassifyDto> listV2(@Param("t_paper_classify") String tableName, @Param("ew") QueryWrapper<TPaperClassify> ew);
}
