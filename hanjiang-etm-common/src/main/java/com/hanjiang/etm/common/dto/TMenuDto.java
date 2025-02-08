package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TMenu;
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
public class TMenuDto extends TMenu implements TreeNode<Long> {
    private List<TMenuDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TMenuDto>)children;
    }

    @Override
    public Object clone() {
        TMenuDto organization;
        try {
            organization = (TMenuDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMenuDto that = (TMenuDto) o;
        return Objects.equals(id, that.id);
    }
}
