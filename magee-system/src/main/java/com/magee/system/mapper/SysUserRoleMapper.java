package com.magee.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.system.domain.SysRole;
import com.magee.system.domain.SysUser;
import com.magee.system.domain.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author magee
* @description  sys_user_role(用户角色) 的数据库操作Mapper
* @Entity com.magee.system.domain.SysUserRole
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 查询角色分配的用户
     * */
    IPage<SysUser> selectUserByRoleId(IPage<SysUser> page, @Param("ew")QueryWrapper<?> queryWrapper , @Param("roleId")Long roleId);

    /**
     * 查询角色分配的用户
     * */
    IPage<SysRole> selectRoleByUserId(IPage<SysRole> page, @Param("ew")QueryWrapper<?> queryWrapper , @Param("userId")Long userId);
}




