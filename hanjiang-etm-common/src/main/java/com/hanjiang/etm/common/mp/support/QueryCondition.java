package com.hanjiang.etm.common.mp.support;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

/**
 * 描述：查询条件
 *
 * @author huxuehao
 */
public enum QueryCondition {
    GT(">", "gt", "大于"),
    GE(">=", "ge", "大于等于"),
    LT("<", "lt", "小于"),
    LE("<=", "le", "小于等于"),
    EQ("=", "eq", "等于"),
    NE("!=", "ne", "不等于"),
    IN("IN", "in", "包含"),
    LIKE("LIKE", "like", "全模糊"),
    LEFT_LIKE("LEFT_LIKE", "likeleft", "左模糊"),
    RIGHT_LIKE("RIGHT_LIKE", "likeright", "右模糊"),
    SQL_RULES("USE_SQL_RULES", "ext", "自定义SQL片段"),
    IS_NULL("is null", "null", ""),
    IS_NOT_NULL("is not null", "notnull", "is not null"),
    HAVING("having", "having", "having"),
    EXISTS("exists", "exists", "exists"),
    BETWEEN("between", "between", "between");

    private String value;
    private String condition;
    private String msg;

    private QueryCondition(String value, String condition, String msg) {
        this.value = value;
        this.condition = condition;
        this.msg = msg;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public static QueryCondition getByValue(String value) {
        if (ObjectUtils.isNotEmpty(value)) {
            for (QueryCondition sqlOperator : values()) {
                if (sqlOperator.getValue().equals(value) || sqlOperator.getCondition().equals(value)) {
                    return sqlOperator;
                }
            }
        }
        return null;
    }
}
