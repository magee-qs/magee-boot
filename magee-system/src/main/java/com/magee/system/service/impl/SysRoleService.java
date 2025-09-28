package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.system.domain.SysRole;
import com.magee.system.domain.SysUser;
import com.magee.system.domain.SysUserRole;
import com.magee.system.model.RoleOfUserModel;
import com.magee.system.model.UserOfRoleModel;
import com.magee.system.query.RoleParam;
import com.magee.system.query.UserParam;
import com.magee.system.service.ISysRoleService;
import com.magee.system.mapper.SysRoleMapper;
import com.magee.system.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author magee
* @description  sys_role(角色) 的数据库操作Service实现
*/
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole>
    implements ISysRoleService {

    @Autowired
    private IUserRoleService roleService;
    /**
     * 修改状态
     * */
    public void changeStatus(Long roleId){
        LambdaUpdateWrapper<SysRole> updateWrapper  = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRole::getRoleId, roleId)
                .setSql("status = 1 - status");

        update(updateWrapper);
    }

    /**
     * 查询角色分配的用户
     * */
    public IPage<SysUser> getUserByRoleId(UserParam userParam){
        QueryWrapper<SysUser> queryWrapper = SimpleQuery.toQueryWrapper(userParam,"u","roleId");

        IPage<SysUser> page = SimpleQuery.toPage("u");
        page = roleService.selectUserByRoleId(page, queryWrapper, userParam.getRoleId());
        return page;
    }

    /**
     * 保存角色用户
     * */
    @Transactional
    public void saveUser(RoleOfUserModel userModel){
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, userModel.getRoleId())
                .in(SysUserRole::getUserId, userModel.getUserIds());
        // 删除
        roleService.remove(wrapper);

        List<SysUserRole> list = userModel.getUserIds().stream()
                .map(userId -> {
                    SysUserRole entity = new SysUserRole();
                    entity.setRoleId(userModel.getRoleId());
                    entity.setUserId(userId);
                    return entity;
                }).collect(Collectors.toList());

        roleService.saveBatch(list);
    }

    /**
     * 删除角色关联的用户
     * */
    public void removeUser(RoleOfUserModel userModel){
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, userModel.getRoleId())
                .in(SysUserRole::getUserId, userModel.getUserIds());
        // 删除
        roleService.remove(wrapper);
    }



    /**
     * 查询角色分配的用户
     * */
    public IPage<SysRole> getRoleByUserId(RoleParam roleParam){
        QueryWrapper<SysUser> queryWrapper =  SimpleQuery.toQueryWrapper(roleParam,"r", "userId");
        IPage<SysRole> page = SimpleQuery.toPage("r");
        page = roleService.selectRoleByUserId(page, queryWrapper, roleParam.getUserId());
        return page;
    }



    /**
     * 保存角色用户
     * */
    @Transactional
    public void saveRole(UserOfRoleModel roleModel){
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, roleModel.getUserId())
                .in(SysUserRole::getRoleId, roleModel.getRoleIds());
        // 删除
        roleService.remove(wrapper);

        List<SysUserRole> list = roleModel.getRoleIds().stream()
                .map(roleId -> {
                    SysUserRole entity = new SysUserRole();
                    entity.setRoleId(roleId);
                    entity.setUserId(roleModel.getUserId());
                    return entity;
                }).collect(Collectors.toList());

        roleService.saveBatch(list);
    }


    /**
     * 删除用户关联的角色
     * */
    public void removeRole(UserOfRoleModel roleModel){
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, roleModel.getUserId())
                .in(SysUserRole::getRoleId, roleModel.getRoleIds());
        // 删除
        roleService.remove(wrapper);
    }
}




