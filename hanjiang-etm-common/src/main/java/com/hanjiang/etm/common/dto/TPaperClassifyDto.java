package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TPaperClassify;
import com.hanjiang.etm.common.util.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 描述：试卷分类DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TPaperClassifyDto extends TPaperClassify implements TreeNode<Long> {

    private List<TPaperClassifyDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TPaperClassifyDto>)children;
    }

    @Override
    public List<? extends TreeNode<Long>> getChildren() {
        return children;
    }

    @Override
    public Object clone() {
        TPaperClassifyDto dto;
        try {
            dto = (TPaperClassifyDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}
