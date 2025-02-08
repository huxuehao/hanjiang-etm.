package com.hanjiang.etm.params.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.entity.TParams;
import com.hanjiang.etm.params.mapper.TParamsMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：参数校验适配器
 *
 * @author huxuehao
 **/
@Component
public class ParamsAdapter {
    // 注册表
    private static final Map<String, ParamsCore> registryMap = new ConcurrentHashMap<>();

    private final TParamsMapper paramsMapper;
    public ParamsAdapter(TParamsMapper paramsMapper) {
        this.paramsMapper = paramsMapper;
    }

    public void register(String key, ParamsCore paramsCore) {
        assert key != null : "transferEnum 不可为空";
        registryMap.put(key, paramsCore);
    }

    public String getValue(String key) {
        QueryWrapper<TParams> qw = new QueryWrapper<>();
        qw.eq("param_key", key);
        qw.eq("del_flag", 0);
        ParamsCore paramsCore = registryMap.get(key);
        try {
            TParams params = paramsMapper.selectOne(qw);
            if (params == null) {
                return paramsCore == null ? null : paramsCore.getDefaultValue();
            }
            if (paramsCore == null) {
                return params.getParamValue();
            }
            return paramsCore.checkAndFormatValue(params.getParamValue());
        } catch (Exception e) {
            throw new RuntimeException("不存在唯一Key:" + key, e);
        }
    }
    public int saveParams(TParams params) {
        ParamsCore paramsCore = registryMap.get(params.getParamKey());
        if (paramsCore != null) {
            params.setParamValue(paramsCore.checkAndFormatValue(params.getParamValue()));
        }
        return paramsMapper.insert(params);
    }

    public int updateParams(TParams params) {
        ParamsCore paramsCore = registryMap.get(params.getParamKey());
        if (paramsCore != null) {
            params.setParamValue(paramsCore.checkAndFormatValue(params.getParamValue()));
        }
        return paramsMapper.updateById(params);
    }
}
