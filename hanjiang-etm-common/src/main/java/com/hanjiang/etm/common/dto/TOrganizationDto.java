package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TOrganization;
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
public class TOrganizationDto extends TOrganization implements TreeNode<Long> {
    private List<TOrganizationDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TOrganizationDto>)children;
    }

    @Override
    public Object clone() {
        TOrganizationDto organization;
        try {
            organization = (TOrganizationDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TOrganizationDto that = (TOrganizationDto) o;
        return Objects.equals(id, that.id);
    }
}
