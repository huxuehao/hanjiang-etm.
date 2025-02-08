package com.hanjiang.etm.train.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.entity.TTraining;
import com.hanjiang.etm.common.vo.ExamTrainInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：培训
 *
 * @author huxuehao
 **/
@Mapper
public interface TTrainingMapper extends BaseMapper<TTraining> {
    List<ExamTrainInfo> selectTrainInfo(@Param("dbName") String dbName, @Param("currentTime") String currentTime);
}
