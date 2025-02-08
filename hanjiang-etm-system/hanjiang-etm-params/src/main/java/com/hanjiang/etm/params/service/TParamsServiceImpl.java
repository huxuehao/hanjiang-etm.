package com.hanjiang.etm.params.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TParams;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.params.mapper.TParamsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
@Service
public class TParamsServiceImpl extends ServiceImpl<TParamsMapper, TParams> implements TParamsService {
    private final ParamsAdapter paramsAdapter;

    public TParamsServiceImpl(ParamsAdapter paramsAdapter) {
        this.paramsAdapter = paramsAdapter;
    }

    @Override
    public String fetchValueByKey(String key) {
        return paramsAdapter.getValue(key);
        //QueryWrapper<TParams> qw = new QueryWrapper<>();
        //qw.eq("param_key", key);
        //qw.eq("del_flag", 0);
        //try {
        //    return getOne(qw).getParamValue();
        //} catch (Exception e) {
        //    throw new RuntimeException("不存在唯一Key:" + key, e);
        //}
    }

    @Override
    public boolean saveV2(TParams params) {
        if(Func.isEmpty(params.getParamValue())) {
            throw new RuntimeException("value值不可为空");
        }
        QueryWrapper<TParams> qw = new QueryWrapper<>();
        qw.eq("param_key", params.getParamKey());
        qw.eq("del_flag", 0);
        List<TParams> list = list(qw);
        if (list == null || list.isEmpty()) {
            return paramsAdapter.saveParams(params) > 0;
            //return save(params);
        } else {
            throw new RuntimeException("Key已存在");
        }
    }


    @Override
    public boolean updateByIdV2(TParams params) {
        if(Func.isEmpty(params.getParamValue())) {
            throw new RuntimeException("value值不可为空");
        }
        QueryWrapper<TParams> qw = new QueryWrapper<>();
        qw.eq("param_key", params.getParamKey());
        qw.eq("del_flag", 0);
        qw.ne("id", params.getId());
        List<TParams> list = list(qw);
        if (list == null || list.isEmpty()) {
            return paramsAdapter.updateParams(params) > 0;
            //return updateById(params);
        } else {
            throw new RuntimeException("Key已存在");
        }
    }
}
