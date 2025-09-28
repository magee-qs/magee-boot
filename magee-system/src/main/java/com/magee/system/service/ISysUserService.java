package com.magee.system.service;

import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.model.UserModel;

/**
* @author magee
* @description  sys_user(用户) 的数据库操作Service
*/
public interface ISysUserService extends IService<SysUser> {
    /**
     * 获取用户个人信息
     * */
    AjaxResult getProfile();
    /**
     * 更新用户
     * */
    SysUser updateProfile(SysUser user);

    /**
     * 修改密码
     * */
    void updatePwd(String oldPassword, String newPassword);

    /**
     * 修改头像
     * @param avatar 图片地址
     * @param userId 用户id
     * */
    void updateAvatar(Long userId, String avatar);


    /**
     * 更新用户
     * */
    void updateUser(UserModel userModel);

    /**
     * 新增用户
     * */
    void addUser(SysUser sysUser);

    /**
     * 修改用户状态
     * */
    void changeStatus(Long userId);


    /**
     * 密码重置
     * */
    void resetPwd(Long userId);
}
