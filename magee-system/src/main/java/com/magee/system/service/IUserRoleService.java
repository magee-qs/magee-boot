package com.magee.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.domain.SysRole;
import com.magee.system.domain.SysUser;
import com.magee.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 用户角色
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public interface IUserRoleService   extends IService<SysUserRole> {
    /**
     * 查询角色分配的用户
     * */
    IPage<SysUser> selectUserByRoleId(IPage<SysUser> page, QueryWrapper<?> queryWrapper ,  Long roleId);

    /**
     * 查询角色分配的用户
     * */
    IPage<SysRole> selectRoleByUserId(IPage<SysRole> page, QueryWrapper<?> queryWrapper , Long userId);
}
