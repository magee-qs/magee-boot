package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.utils.ObjectUtils;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysRole;
import com.magee.system.domain.SysUser;
import com.magee.system.model.RoleOfUserModel;
import com.magee.system.model.UserOfRoleModel;
import com.magee.system.query.RoleParam;
import com.magee.system.query.UserParam;
import com.magee.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/role")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "获取角色列表")
    @RequiresPermissions("system:role:query")
    @GetMapping("/list")
    public AjaxResult list(SysRole role){
        IPage<SysRole> page = SimpleQuery.toPage();
        QueryWrapper<SysRole> queryWrapper =  SimpleQuery.toQueryWrapper(role);
        page = roleService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }


    @ApiOperation("查询角色")
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getRole(@PathVariable Long roleId){
        SysRole role = roleService.getById(roleId);
        return AjaxResult.ok(role);
    }

    @ApiOperation("添加角色")
    @RequiresPermissions("system:role:add")
    @PostMapping("/add")
    public AjaxResult saveRole(@RequestBody SysRole role){
        roleService.save(role);
        return AjaxResult.ok();
    }

    @ApiOperation("修改角色")
    @RequiresPermissions("system:role:edit")
    @PostMapping("/update")
    public AjaxResult updateRole(@RequestBody SysRole role){
        if(ObjectUtils.isNull(role.getRoleId())){
            return AjaxResult.error("角色不存在");
        }
        roleService.updateById(role);
        return AjaxResult.ok();
    }

    @ApiOperation("删除角色")
    @RequiresPermissions("system:role:remove")
    @DeleteMapping("/remove/{roleIds}")
    public AjaxResult removeRoleById(@PathVariable  Long[] roleIds){
        if(ObjectUtils.isNull(roleIds)){
            return AjaxResult.error("缺少参数");
        }
        roleService.removeByIds(Arrays.asList(roleIds));
        return  AjaxResult.ok();
    }

    @ApiOperation("修改角色状态")
    @RequiresPermissions("system:role:edit")
    @GetMapping("/changeStatus/{roleId}")
    public AjaxResult changeStatus(@PathVariable Long roleId){
         roleService.changeStatus(roleId);
         return  AjaxResult.ok();
    }

    @ApiOperation("查询分配角色的用户")
    @RequiresPermissions("system:role:query")
    @GetMapping("/userList")
    public AjaxResult getUserList(UserParam userParam){
        IPage<SysUser> page = roleService.getUserByRoleId(userParam);
        return AjaxResult.list(page);
    }


    @ApiOperation("保存角色用户")
    @PostMapping("/saveUser")
    @RequiresPermissions("system:role:edit")
    public AjaxResult saveUser(@Validated  @RequestBody RoleOfUserModel userModel){
        roleService.saveUser(userModel);
        return AjaxResult.ok();
    }

    @ApiOperation("删除角色用户")
    @PostMapping("/removeUser")
    @RequiresPermissions("system:role:remove")
    public AjaxResult removeUser(@Validated  @RequestBody RoleOfUserModel userModel){
        roleService.removeUser(userModel);
        return  AjaxResult.ok();
    }

    @ApiOperation("查询用户分配的角色")
    @GetMapping("/roleList")
    @RequiresPermissions("system:role:query")
    public AjaxResult getRoleList(RoleParam roleParam){
        IPage<SysRole> page = roleService.getRoleByUserId(roleParam);
        return AjaxResult.list(page);
    }


    @ApiOperation("保存角色用户")
    @PostMapping("/saveRole")
    @RequiresPermissions("system:role:edit")
    public AjaxResult saveRole(@Validated  @RequestBody UserOfRoleModel roleModel){
        roleService.saveRole(roleModel);
        return AjaxResult.ok();
    }

    @ApiOperation("删除角色用户")
    @PostMapping("/removeRole")
    @RequiresPermissions("system:role:remove")
    public AjaxResult removeUserRole(@Validated  @RequestBody UserOfRoleModel roleModel){
        roleService.removeRole(roleModel);
        return  AjaxResult.ok();
    }
}
