package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TRole;
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
public class TRoleDto extends TRole implements TreeNode<Long> {
    private List<TRoleDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TRoleDto>)children;
    }

    @Override
    public Object clone() {
        TRoleDto organization;
        try {
            organization = (TRoleDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TRoleDto that = (TRoleDto) o;
        return Objects.equals(id, that.id);
    }
}
