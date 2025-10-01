package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.common.constant.CacheConstant;
import com.magee.common.constant.CommonConstant;
import com.magee.common.utils.SecurityUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.TreeUtils;
import com.magee.framework.core.vo.UserInfo;
import com.magee.system.domain.SysMenu;
import com.magee.system.domain.SysUser;
import com.magee.system.service.ISysMenuService;
import com.magee.system.mapper.SysMenuMapper;
import com.magee.system.service.ISysUserService;
import com.magee.system.vo.MetaVO;
import com.magee.system.vo.RouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* @author magee
* @description  sys_menu(菜单权限) 的数据库操作Service实现
*/
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu>
    implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private ISysUserService userService;

    /**
     *  查询菜单列表
     * */
    public List<SysMenu> getMenuList(Long userId){
        List<SysMenu> menus = null;
        SysUser user = userService.getById(userId);
        String userType = user == null ? "": user.getUserType();

        if(UserInfo.isAdmin(userType)){
            // 管理员
            menus = menuMapper.selectMenuAll();
        }else{
            menus = menuMapper.selectMenuByUserId(userId);
        }
        return menus;
    }

    /**
     * 查询树型菜单
     * */
    public List<SysMenu> getMenuTreeList(Long userId){
        List<SysMenu> menus = null;
        SysUser user = userService.getById(userId);
        String userType = user == null ? "": user.getUserType();
        if(UserInfo.isAdmin(userType)){
            // 管理员
            menus = menuMapper.selectMenuTreeAll();
        }else{
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return menus;
    }

    /**
     * 根据用户ID查询菜单
     * @param userId 用户
     * @return 菜单列表
     * */
    public List<RouteVO> getMenuTreeByUserId(Long userId){
        List<SysMenu> menus = getMenuTreeList(userId);
        menus = TreeUtils.toTree(menus, 0L);
        List<RouteVO> routes = buildMenus(menus);
        return  routes;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    private List<RouteVO> buildMenus(List<SysMenu> menus){
        List<RouteVO> routers = new LinkedList<RouteVO>();
        for (SysMenu menu: menus){
            RouteVO router = new RouteVO();
            router.setHidden(menu.getHidden().equals(1));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO(menu.getName(), menu.getIcon(),  menu.getCache().equals(1), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && "M".equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouteVO> childrenList = new ArrayList<RouteVO>();
                RouteVO children = new RouteVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(),  menu.getCache().equals(1), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
            {
                router.setMeta(new MetaVO(menu.getName(), menu.getIcon()));
                router.setPath("/");
                List<RouteVO> childrenList = new ArrayList<RouteVO>();
                RouteVO children = new RouteVO();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVO(menu.getName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }
    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    private String getRouteName(SysMenu menu){
        // 非外链并且是一级目录（类型为目录）
        if(isMenuFrame(menu)){
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path)
    {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.upperFirst(routerName);
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu)
    {
        return menu.getParentId().intValue() == 0 &&  "C".equals(menu.getMenuType())
                && menu.getFrame().equals(0);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu)
    {
        return menu.getFrame().equals(0) && StringUtils.isHttp(menu.getPath());
    }


    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu)
    {
        return menu.getParentId().intValue() != 0 && "M".equals(menu.getMenuType());
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu)
    {
        String component = "Layout";
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component =  "InnerLink";
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = "ParentView";
        }
        return component;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && "M".equals(menu.getMenuType())
                && menu.getFrame().equals(0))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }
    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { CommonConstant.HTTP, CommonConstant.HTTPS, CommonConstant.WWW, ".", ":" },
                new String[] { "", "", "", "/", "/" });
    }

    /**
     * 添加用户
     * */
    @CacheEvict(value = CacheConstant.SYS_MENU_CACHE, allEntries = true)
    public void addMenu(SysMenu menu){
        save(menu);
    }

    /**
     * 修改用户
     * */
    @CacheEvict(value = CacheConstant.SYS_MENU_CACHE, allEntries = true)
    public void updateMenu(SysMenu menu){
        updateById(menu);
    }


    /**
     * 删除用户
     * */
    @CacheEvict(value = CacheConstant.SYS_MENU_CACHE, allEntries = true)
    public void removeMenu(List<Long> menuIds){
        removeByIds(menuIds);
    }


    /**
     * 缓存读取menu
     * */
    @Cacheable(value = CacheConstant.SYS_MENU_CACHE, key = "#component")
    public  SysMenu getMenuCacheByComponent(String component){
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getComponent, component);

        SysMenu sysMenu = getOne(queryWrapper);
        return sysMenu;
    }

}




