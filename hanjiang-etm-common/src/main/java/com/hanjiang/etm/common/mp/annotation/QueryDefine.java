package com.hanjiang.etm.common.mp.annotation;

import com.hanjiang.etm.common.mp.support.QueryCondition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述：查询定义
 *
 * @author huxuehao
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryDefine {
    String value() default "描述";
    QueryCondition condition();
}
