package com.magee.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 权限配置
 * @TableName sys_permission
 */
@TableName(value ="sys_permission")
@Data
public class SysPermission implements Serializable {
    public SysPermission(){}

    public SysPermission(Integer itemType, Long itemId, Long menuId){
        this.itemId = itemId;
        this.menuId = menuId;
        this.itemType = itemType;
    }

    /**
     * 
     */
    @TableId
    private Long permId;

    /**
     * 类型 1 角色 2 用户
     */
    private Integer itemType;

    /**
     * 对象id
     */
    private Long itemId;

    /**
     * 菜单id
     */
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}