package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.EnumUtils;
import com.magee.framework.core.controller.BaseController;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysPermission;
import com.magee.system.enums.PermissionItemType;
import com.magee.system.model.MenuSelectNode;
import com.magee.system.model.PermissionModel;
import com.magee.system.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能描述: 权限操作
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/permission")
@Api(tags = "权限配置")
public class PermissionController extends BaseController {

    @Autowired
    private IPermissionService permissionService;


    @ApiOperation("菜单权限树")
    @GetMapping("/menuTree")
    public AjaxResult getMenuTree(){
        List<MenuSelectNode> nodes = permissionService.getMenuTree();
        return AjaxResult.ok(nodes);
    }
    /**
     * 获取item权限
     * */
    @GetMapping("/{itemId}")
    @ApiOperation("获取操作权限")
    public AjaxResult getPermission(@PathVariable Long itemId){
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPermission::getItemId, itemId);
        List<SysPermission> list = permissionService.list(queryWrapper);
        return AjaxResult.ok(list);
    }

    /**
     * 保存操作权限
     * */
    @ApiOperation(value = "保存操作权限")
    @PostMapping("/save")
    public AjaxResult savePermission(@Validated @RequestBody PermissionModel permissionModel){

        PermissionItemType itemTypeEnum = EnumUtils.fromStringQuietly(PermissionItemType.class, permissionModel.getItemType());
        if(itemTypeEnum == null){
            return  AjaxResult.error("授权参数类型不正确");
        }
        permissionService.savePermission(itemTypeEnum, permissionModel.getItemId(), permissionModel.getPermIds());
        return AjaxResult.ok();
    }
}
