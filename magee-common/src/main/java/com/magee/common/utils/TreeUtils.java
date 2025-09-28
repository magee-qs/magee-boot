package com.magee.common.utils;

import cn.hutool.core.lang.tree.TreeUtil;

import java.util.*;

/**
 * 功能描述: 树型结构工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class TreeUtils extends TreeUtil {
    /**
     * 循环遍历构建树形结构
     * */
    public static <T extends   ITreeNode<E>, E> List<T> toTree(List<T>list, E parentId){
        if(list == null || list.size() == 0){
            return  list;
        }
        List<T> result = new ArrayList<>();
        // 使用Map来存储所有节点，key为节点ID，value为节点对象
        Map<E, T> nodeMap = new HashMap<>();
        // 首先将所有节点放入Map中
        for (T node : list) {
            nodeMap.put(node.getId(), node);
        }

        // 遍历所有节点，构建树形结构
        for (T node : list) {
            E nodeParentId = node.getParentId();
            if (Objects.equals(parentId, nodeParentId)) {
                // 如果节点的父ID等于指定的parentId，则将其添加到结果列表（根节点）
                result.add(node);
            } else {
                // 否则找到其父节点，将其添加到父节点的children列表中
                T parentNode = nodeMap.get(nodeParentId);
                if (parentNode != null) {
                    List<T> children = (List<T>) parentNode.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parentNode.setChildren(children);
                    }
                    children.add(node);
                } else {
                    // 如果没有找到父节点，说明该节点可能是游离节点，可以选择添加到根节点或忽略
                    // 这里选择添加到根节点
                    result.add(node);
                }
            }
        }
        return result;
    }


    public interface  ITreeNode<E>{
        E getId();

        E getParentId();

        void setChildren(List<? extends ITreeNode<E>> children);

        List<? extends ITreeNode<E>> getChildren();

    }
}
