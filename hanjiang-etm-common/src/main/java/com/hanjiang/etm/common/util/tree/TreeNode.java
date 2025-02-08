package com.hanjiang.etm.common.util.tree;

import java.util.List;

/**
 * 描述：Tree node 节点接口类节点
 *
 * @author huxuehao
 */

public interface TreeNode<T> extends Cloneable {

    /* 设置ID */
    void setId(T id);

    /* 获取ID */
    T getId();

    /* 设置ID */
    void setParentId(T parentId);

    /* 获取ID */
    T getParentId();


    /* 设置节点的子节点列表 */
    void setChildren(List<? extends TreeNode<?>> children);

    /* 获取所有子节点 */
    List<? extends TreeNode<T>> getChildren();

    /* 拷贝, 自定义深拷贝还是浅深拷贝 **/
    Object clone();

}
