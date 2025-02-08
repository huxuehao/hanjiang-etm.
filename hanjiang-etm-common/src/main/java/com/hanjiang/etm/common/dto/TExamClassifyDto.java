package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TExamClassify;
import com.hanjiang.etm.common.util.tree.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TExamClassifyDto extends TExamClassify implements TreeNode<Long> {

    private List<TExamClassifyDto> children;


    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TExamClassifyDto>) children;
    }

    @Override
    public List<? extends TreeNode<Long>> getChildren() {
        return children;
    }

    @Override
    public Object clone() {
        TExamClassifyDto dto;

        try {
            dto = (TExamClassifyDto) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return null;
    }
}
