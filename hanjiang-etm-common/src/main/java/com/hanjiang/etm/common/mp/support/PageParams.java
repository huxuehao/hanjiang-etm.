package com.hanjiang.etm.common.mp.support;

import java.util.List;

/**
 * 描述：分页参数
 *
 * @author huxuehao
 */
public class PageParams {
    private Integer current;
    private Integer size;
    private List<String> asc;
    private List<String> desc;

    public PageParams() {
    }

    public Integer getCurrent() {
        return this.current;
    }

    public Integer getSize() {
        return this.size;
    }

    public PageParams setCurrent(final Integer current) {
        this.current = current;
        return this;
    }

    public PageParams setSize(final Integer size) {
        this.size = size;
        return this;
    }

    public List<String> getAsc() {
        return asc;
    }

    public void setAsc(List<String> asc) {
        this.asc = asc;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }
}
