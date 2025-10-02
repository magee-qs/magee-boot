package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.annotation.PermissionData;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysUser;
import com.magee.system.model.UserModel;
import com.magee.system.query.UserParam;
import com.magee.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户管理")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    @RequiresPermissions("system:user:query") 
    public AjaxResult list(UserParam userParam){
        QueryWrapper<SysUser> queryWrapper = SimpleQuery.toQueryWrapper(userParam);
        IPage<SysUser> page = SimpleQuery.toPage();
        page = userService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }


    @ApiOperation("添加用户")
    @RequiresPermissions("system:user:add")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysUser sysUser){
        userService.addUser(sysUser);
        return AjaxResult.ok();
    }

    @ApiOperation("修改用户")
    @RequiresPermissions("system:user:edit")
    @PostMapping("/update")
    public AjaxResult update(@Validated @RequestBody UserModel userModel){
        userService.updateUser(userModel);
        return AjaxResult.ok();
    }

    @ApiOperation("获取用户")
    @GetMapping("/{userId}")
    @RequiresPermissions("system:user:query")
    public AjaxResult getUser(@PathVariable Long userId){
        SysUser user = userService.getById(userId);
        return AjaxResult.ok(user);
    }

    @ApiOperation("删除用户")
    @GetMapping("/remove/{userId}")
    @RequiresPermissions("system:user:remove")
    public AjaxResult remove(@PathVariable Long userId){
        userService.removeById(userId);
        return AjaxResult.ok( );
    }

    @ApiOperation("修改状态")
    @GetMapping("/changeStatus/{userId}")
    @RequiresPermissions("system:user:edit")
    public AjaxResult changeStatus(@PathVariable Long userId){
         userService.changeStatus(userId);
         return AjaxResult.ok();
    }

    @ApiOperation("重置密码")
    @GetMapping("/resetPwd/{userId}")
    @RequiresPermissions("system:user:edit")
    public AjaxResult resetPwd(@PathVariable Long userId){
        userService.resetPwd(userId);
        return AjaxResult.ok();
    }
}
