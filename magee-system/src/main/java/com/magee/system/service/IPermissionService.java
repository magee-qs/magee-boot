package com.magee.system.service;

import com.magee.system.domain.SysMenu;
import com.magee.system.domain.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.enums.PermissionItemType;
import com.magee.system.model.MenuSelectNode;

import java.util.List;

/**
* @author magee
* @description 针对表【sys_permission(权限配置)】的数据库操作Service
*/
public interface IPermissionService extends IService<SysPermission> {
    /**
     * 菜单权限树
     * */
    /** 获取菜单权限树 */
    List<MenuSelectNode> getMenuTree();

    /**
     * 保存操作权限
     * */
    void savePermission(PermissionItemType itemType, Long itemId , Long[] permIds);
}
