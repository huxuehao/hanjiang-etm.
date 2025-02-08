package com.hanjiang.etm.question.factory.enums;

/**
 * 描述：正确类型
 *
 * @author huxuehao
 **/
public enum CorrectType {
    ERROR(0, "错误"),
    SUCCESS(1, "正确"),
    WARN(2, "异常");

    public final int value;
    public final String desc;

    CorrectType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
