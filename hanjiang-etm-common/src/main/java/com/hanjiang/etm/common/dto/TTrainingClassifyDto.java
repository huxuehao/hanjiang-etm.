package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TTrainingClassify;
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
public class TTrainingClassifyDto extends TTrainingClassify implements TreeNode<Long> {

    private List<TTrainingClassifyDto> children;

    @Override
    public void setChildren(List<? extends TreeNode<?>> children) {
        this.children = (List<TTrainingClassifyDto>)children;
    }

    @Override
    public List<? extends TreeNode<Long>> getChildren() {
        return children;
    }

    @Override
    public Object clone() {
        TTrainingClassifyDto dto;
        try {
            dto = (TTrainingClassifyDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}
