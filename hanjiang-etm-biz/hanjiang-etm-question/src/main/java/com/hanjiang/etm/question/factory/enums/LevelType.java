package com.hanjiang.etm.question.factory.enums;

/**
 * 描述：难度类型
 *
 * @author huxuehao
 **/
public enum LevelType {
    one(1, "一星"),
    two(2, "二星"),
    three(3, "三星"),
    four(4, "四星"),
    five(5, "五星");

    public final int value;
    public final String desc;

    LevelType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
