package com.magee.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magee.common.annotation.DictField;
import com.magee.common.utils.TreeUtils;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 菜单权限
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class SysMenu extends BaseEntity implements TreeUtils.ITreeNode<Long> {
    /**
     * 
     */
    @TableId
    private Long menuId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 序号
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 外链 0 否 1 是
     */
    private Integer frame;

    /**
     * 缓存 0 否 1 是
     */
    private Integer cache;

    /**
     * 菜单类型 M目录 C 菜单 F 按钮
     */
    private String menuType;

    /**
     * 隐藏 0 否 1 是
     */
    private Integer hidden;

    /**
     * 状态 0 否 1 是
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 操作权限
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    @JsonIgnore
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


    @Override
    public Long getId() {
        return menuId;
    }

    @Override
    public void setChildren(List<? extends TreeUtils.ITreeNode<Long>> children) {
        this.children = (List<SysMenu>) children;
    }
}