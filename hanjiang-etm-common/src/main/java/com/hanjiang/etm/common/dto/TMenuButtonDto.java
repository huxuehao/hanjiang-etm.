package com.hanjiang.etm.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hanjiang.etm.common.entity.TMenuButton;
import com.hanjiang.etm.common.util.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TMenuButtonDto extends TMenuButton implements TreeNode<Long> {
    private List<TMenuButtonDto> children;
    @JsonSerialize(using = ToStringSerializer.class)
    public Long parentId;
    private boolean disabled;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TMenuButtonDto>)children;
    }

    @Override
    public Object clone() {
        TMenuButtonDto organization;
        try {
            organization = (TMenuButtonDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMenuButtonDto that = (TMenuButtonDto) o;
        return Objects.equals(id, that.id);
    }
}
