package com.magee.system.service;

import com.magee.common.constant.CacheConstant;
import com.magee.system.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.vo.RouteVO;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

/**
* @author magee
* @description  sys_menu(菜单权限) 的数据库操作Service
*/
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 根据用户ID查询菜单
     * @param userId 用户
     * @return 菜单列表
     * */
    List<RouteVO> getMenuTreeByUserId(Long userId);


    /**
     *  查询菜单列表
     * */
    List<SysMenu> getMenuList(Long userId);

    /**
     * 查询树型菜单
     * */
    List<SysMenu> getMenuTreeList(Long userId);


    /**
     * 添加用户
     * */
    public void addMenu(SysMenu menu) ;

    /**
     * 修改用户
     * */
    public void updateMenu(SysMenu menu) ;


    /**
     * 删除用户
     * */

    void removeMenu(List<Long> menuIds);


    /**
     * 缓存读取menu
     * */
    SysMenu getMenuCacheByComponent(String component);
}
