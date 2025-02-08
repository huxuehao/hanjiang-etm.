package com.hanjiang.etm.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.entity.TExam;
import com.hanjiang.etm.common.vo.ExamTrainInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Mapper
public interface TExamMapper extends BaseMapper<TExam> {
    List<ExamTrainInfo> selectExamInfo(@Param("dbName") String dbName, @Param("currentTime") String currentTime);
}
