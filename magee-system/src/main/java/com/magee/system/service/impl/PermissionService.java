package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.common.constant.CacheConstant;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.TreeUtils;
import com.magee.system.domain.SysMenu;
import com.magee.system.domain.SysPermission;
import com.magee.system.enums.PermissionItemType;
import com.magee.system.mapper.SysMenuMapper;
import com.magee.system.model.MenuSelectNode;
import com.magee.system.service.IPermissionService;
import com.magee.system.mapper.SysPermissionMapper;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author magee
* @description 针对表【sys_permission(权限配置)】的数据库操作Service实现
*/
@Service
public class PermissionService extends ServiceImpl<SysPermissionMapper, SysPermission>
    implements IPermissionService {

    @Autowired
    private SysMenuMapper menuMapper;

    private SysPermissionMapper permissionMapper;

    /**
     * 菜单权限树
     * */
    @Cacheable(value = CacheConstant.SYS_MENU_CACHE, key = "'ALL'")
    public List<SysMenu> getMenuList(){
        List<SysMenu> menus = menuMapper.selectList(new QueryWrapper<>());
        return menus;
    }

    /** 获取菜单权限树 */
    public List<MenuSelectNode> getMenuTree(){
        List<SysMenu> menus = getMenuList();
        List<MenuSelectNode> nodes = new ArrayList<>();
        for(SysMenu menu: menus){
            MenuSelectNode node = new MenuSelectNode(menu);
            nodes.add(node);
        }

        // 转换为树形结构
        nodes = TreeUtils.toTree(nodes, 0L);
        return nodes;
    }

    /**
     * 保存操作权限
     * */
    @Transactional
    public  void savePermission(PermissionItemType itemType, Long itemId , Long[] permIds){
        // 删除关联权限
        remove(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getItemId, itemId));
        List<SysPermission> list = new ArrayList<>();
        for(Long permId : permIds){
            SysPermission permission = new SysPermission(itemType.getValue(),itemId, permId);
            list.add(permission);
        }

        saveBatch(list);
    }
}




