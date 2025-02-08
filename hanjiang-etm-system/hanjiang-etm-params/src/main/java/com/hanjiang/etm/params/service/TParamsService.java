package com.hanjiang.etm.params.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TParams;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
public interface TParamsService extends IService<TParams> {
    String fetchValueByKey(String key);

    boolean saveV2(TParams params);
    boolean updateByIdV2(TParams params);
}
