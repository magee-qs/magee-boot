package com.magee.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.system.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.domain.SysUser;
import com.magee.system.model.RoleOfUserModel;
import com.magee.system.model.UserOfRoleModel;
import com.magee.system.query.RoleParam;
import com.magee.system.query.UserParam;
import org.springframework.transaction.annotation.Transactional;

/**
* @author magee
* @description  sys_role(角色) 的数据库操作Service
*/
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 修改状态
     * */
    void changeStatus(Long roleId);


    /**
     * 查询角色分配的用户
     * */
    IPage<SysUser> getUserByRoleId(UserParam userParam);


    /**
     * 保存用户角色
     * */
    void saveUser(RoleOfUserModel userModel);


    /**
     * 删除用户角色
     * */
    void removeUser(RoleOfUserModel userModel);


    /**
     * 查询角色分配的用户
     * */
    IPage<SysRole> getRoleByUserId(RoleParam roleParam);



    /**
     * 保存角色用户
     * */
    @Transactional
    void saveRole(UserOfRoleModel roleModel);


    /**
     * 删除用户关联的角色
     * */
    void removeRole(UserOfRoleModel roleModel);
}
