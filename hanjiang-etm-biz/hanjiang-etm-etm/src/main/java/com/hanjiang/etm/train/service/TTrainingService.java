package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TTrainingDto;
import com.hanjiang.etm.common.entity.TTraining;
import com.hanjiang.etm.common.vo.TTrainingVo;

import java.util.List;

/**
 * 描述：培训
 *
 * @author huxuehao
 **/
public interface TTrainingService extends IService<TTraining> {
    // 保存
    boolean saveV2(TTrainingDto trainingDto);
    // 更新
    boolean updateByIdV2(TTrainingDto trainingDto);
    // 删除
    boolean removeBatchByIdsV2(List<Long> ids);
    // 根据ID获取
    TTrainingVo getByIdV2(Long id);
    // 发布
    boolean publish(Long id);
    // 取消发布
    boolean cancelPublish(Long id, int force);
    // 根据用户ID和培训ID获取用户培训配置
    TTrainingVo getUserTrainConfig(Long trainId, Long userId);
    // 检测培训是否超时
    boolean checkTrainTimeout(TTraining training);
    // 检测培训是否超时
    boolean checkTrainNotStart(TTraining training);
    //用户交卷
    Object paperAnswer(TTrainingVo body);
}
