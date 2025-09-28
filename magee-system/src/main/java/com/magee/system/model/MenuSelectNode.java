package com.magee.system.model;

import com.magee.common.utils.TreeUtils;
import com.magee.system.domain.SysMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 菜单权限选中树形结构
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class MenuSelectNode implements   TreeUtils.ITreeNode<Long>{

    private Long menuId;

    private String label;

    private Long parentId;

    private String perms;

    private String menuType;

    private List<MenuSelectNode> children = new ArrayList<>();


    public MenuSelectNode(){}

    public MenuSelectNode(SysMenu menu){
        this.menuId = menu.getMenuId();
        this.label = menu.getName();
        this.parentId = menu.getParentId();
        this.perms = menu.getPerms();
        // 菜单类型
        this.menuType = menu.getMenuType();
    }



    @Override
    public Long getId() {
        return menuId;
    }
    @Override
    public void setChildren(List<? extends TreeUtils.ITreeNode<Long>> children) {
            this.children = (List<MenuSelectNode>) children;
    }

    @Override
    public List<? extends TreeUtils.ITreeNode<Long>> getChildren() {
         return children;
    }
}
