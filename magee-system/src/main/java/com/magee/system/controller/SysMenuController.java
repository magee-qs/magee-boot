package com.magee.system.controller;

import com.magee.common.annotation.AutoLog;
import com.magee.common.enums.BusinessType;
import com.magee.common.utils.SecurityUtils;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysMenu;
import com.magee.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "菜单管理")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;


    @ApiOperation("查询菜单列表")
    @GetMapping("/list")
    @AutoLog(businessType = BusinessType.QUERY, title = "查询菜单列表")
    public AjaxResult list(SysMenu sysMenu){
        List<SysMenu> menus =  menuService.getMenuList(SecurityUtils.getUserId());
        return AjaxResult.ok(menus);
    }


    @ApiOperation("获取菜单")
    @GetMapping("/{menuId}")
    public AjaxResult getMenuById(@PathVariable Long menuId){
        SysMenu menu = menuService.getById(menuId);
        return AjaxResult.ok(menu);
    }

    @ApiOperation("添加菜单权限")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysMenu menu){
        menuService.save(menu);
        return AjaxResult.ok();
    }

    @ApiOperation("修改菜单权限")
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody SysMenu menu){
        menuService.updateById(menu);
        return AjaxResult.ok();
    }

    @ApiOperation("删除菜单")
    @GetMapping("/remove")
    public AjaxResult remove(@RequestParam List<Long> menuId){
        menuService.removeByIds(menuId);
        return AjaxResult.ok();
    }

}
