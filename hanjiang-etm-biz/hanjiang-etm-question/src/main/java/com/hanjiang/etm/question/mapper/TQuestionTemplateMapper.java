package com.hanjiang.etm.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.entity.TQuestionTemplate;
import com.hanjiang.etm.common.vo.QuestionLevel;
import com.hanjiang.etm.common.vo.QuestionTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：试题模版
 *
 * @author huxuehao
 **/
@Mapper
public interface TQuestionTemplateMapper extends BaseMapper<TQuestionTemplate> {

    List<QuestionTypeVo> selectQuestionType(@Param("dbName") String dbName);
    List<QuestionLevel> selectQuestionLevel(@Param("dbName") String dbName, @Param("level") int level);
}
