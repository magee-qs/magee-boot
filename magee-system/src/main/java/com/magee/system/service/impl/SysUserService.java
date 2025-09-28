package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.common.exception.ServiceException;
import com.magee.common.exception.ValidateException;
import com.magee.common.utils.RandomUtils;
import com.magee.common.utils.SecurityUtils;
import com.magee.common.utils.StringUtils;
import com.magee.framework.config.ApplicationConfig;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysUser;
import com.magee.system.model.UserModel;
import com.magee.system.service.ISysUserService;
import com.magee.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

/**
* @author magee
* @description sys_user(用户)的数据库操作Service实现
*/
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser>
    implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    /**
     * 获取用户个人信息
     * */
    public AjaxResult getProfile(){
        SysUser user =  getById(SecurityUtils.getUserId());
        if(user == null){
            throw new ServiceException("用户不存在");
        }
        AjaxResult result = AjaxResult.ok(user);
        result.put("roleGroup", new ArrayList<String>());
        result.put("postGroup", new ArrayList<String>());
        return result;
    }




    /**
     * 更新用户
     * */
    public SysUser updateProfile(SysUser user){
        SysUser sysUser = getById(SecurityUtils.getUserId());
        if(sysUser == null){
            throw new ServiceException("用户不存在");
        }
        // 根据需求设定
        //user.setNickName(user.getNickName());
        sysUser.setEmail(user.getEmail());
        sysUser.setPhone(user.getPhone());
        sysUser.setSex(user.getSex());

        // 校验邮箱和手机号是否重复
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper <>();
        updateWrapper.eq(SysUser::getUserId, sysUser.getUserId())
                        //.set(SysUser::getNickName, user.getNickName())
                        .set(SysUser::getPhone, user.getPhone())
                        .set(SysUser::getSex,user.getSex());
        userMapper.update(null, updateWrapper);
        return user;
    }

    /**
     * 修改密码
     * */
    public void updatePwd(String oldPassword, String newPassword){
        if(StringUtils.isEmpty(newPassword)){
            throw new ValidateException("密码不能为空");
        }
        String slat = RandomUtils.randomString(RandomUtils.BASE_CHAR_NUMBER_LOWER,6);
        // 密码校验
        String pwd = SecurityUtils.encryptPwd(newPassword , slat);

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, SecurityUtils.getUserId())
                        .set(SysUser::getPassword, pwd)
                        .set(SysUser::getSalt, slat)
                        .set(SysUser::getPwdUpdateTime, new Date());
        userMapper.update(null,updateWrapper);
    }



    /**
     * 修改头像
     * @param avatar 图片地址
     * @param userId 用户id
     * */
    public void updateAvatar(Long userId, String avatar){
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, userId)
                .set(SysUser::getAvatar, avatar);
        userMapper.update(null, updateWrapper);
    }

    /**
     * 添加用户
     * */
    public void addUser(SysUser sysUser){
        // 生成盐值
        String salt = RandomUtils.randomString(RandomUtils.BASE_CHAR_NUMBER_LOWER,6);
        // 密码校验
        String pwd = SecurityUtils.encryptPwd(ApplicationConfig.defaultPwd() , salt);

        sysUser.setPassword(pwd);
        sysUser.setSalt(salt);
        // 默认用户类型
        sysUser.setUserType("00");
        save(sysUser);
    }

    /**
     * 更新用户
     * */
    public void updateUser(UserModel userModel){
        // 密码字段不可修改
        UpdateWrapper<SysUser> updateWrapper = SimpleQuery.toUpdateWrapper(userModel,
                "userName" );
        update(updateWrapper);
    }

    /**
     * 修改用户状态
     * */
    public void changeStatus(Long userId){
        SysUser user = getById(userId);
        if(user == null)
            return;

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);
        wrapper.set(SysUser::getStatus, 1 - user.getStatus());

        update(wrapper);
    }

    /**
     * 密码重置
     * */
    public void resetPwd(Long userId){
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);

        // 生成盐值
        String slat = RandomUtils.randomString(RandomUtils.BASE_CHAR_NUMBER_LOWER,6);
        // 密码校验
        String pwd = SecurityUtils.encryptPwd(ApplicationConfig.defaultPwd() , slat);

        wrapper.set(SysUser::getPassword, pwd);
        wrapper.set(SysUser::getSalt, slat);

        update(wrapper);
    }
}




