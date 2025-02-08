package com.hanjiang.etm.common.function;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 描述：自定义函数式接口，自定义查询条件
 *
 * @author huxuehao
 **/
@FunctionalInterface
public interface QueryWrapperFunction {
    void setCondition(QueryWrapper<?> qw);
}
