package com.hanjiang.etm.question.factory;

import lombok.Data;

/**
 * 描述：选项
 *
 * @author huxuehao
 **/
@Data
public class Option {
    private String title;
    private String value;
    private int selected;
    private int sort;

    public Option() {
    }

    public Option(String title, String value, int selected, int sort) {
        this.title = title;
        this.value = value;
        this.selected = selected;
        this.sort = sort;
    }
}
