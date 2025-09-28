package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.system.domain.SysRole;
import com.magee.system.domain.SysUser;
import com.magee.system.domain.SysUserRole;
import com.magee.system.mapper.SysUserRoleMapper;
import com.magee.system.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述: 用户角色
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Service
public class UserRoleService  extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements IUserRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;
    /**
     * 查询角色分配的用户
     * */
    public IPage<SysUser> selectUserByRoleId(IPage<SysUser> page, QueryWrapper<?> queryWrapper , Long roleId){
        return userRoleMapper.selectUserByRoleId(page, queryWrapper,roleId);
    }

    /**
     * 查询用户分配的角色
     * */
    public IPage<SysRole> selectRoleByUserId(IPage<SysRole> page, QueryWrapper<?> queryWrapper , Long userId){
        return userRoleMapper.selectRoleByUserId(page, queryWrapper,userId);
    }
}
