package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TQuestionClassify;
import com.hanjiang.etm.common.util.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 描述：试题分类DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TQuestionClassifyDto extends TQuestionClassify implements TreeNode<Long> {

    private List<TQuestionClassifyDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TQuestionClassifyDto>)children;
    }

    @Override
    public List<? extends TreeNode<Long>> getChildren() {
        return children;
    }

    @Override
    public Object clone() {
        TQuestionClassifyDto dto;
        try {
            dto = (TQuestionClassifyDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}
