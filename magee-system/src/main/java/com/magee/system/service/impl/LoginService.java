package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.magee.common.exception.ServiceException;
import com.magee.common.utils.ServletUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.TokenUtils;
import com.magee.common.utils.bean.BeanUtils;
import com.magee.common.utils.encrypt.EncryptUtils;
import com.magee.common.utils.encrypt.MD5Util;
import com.magee.common.utils.ip.IpUtils;
import com.magee.framework.config.ApplicationConfig;
import com.magee.framework.config.shiro.JwtUtil;
import com.magee.framework.core.vo.UserInfo;
import com.magee.system.domain.SysUser;
import com.magee.system.mapper.SysUserMapper;
import com.magee.system.model.LoginModel;
import com.magee.system.service.ILoginService;
import com.magee.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Service
public class LoginService implements ILoginService {

    @Autowired
    private ISysUserService userService;
    /**
     * 用户登录
     * */
    @Override
    public String login(LoginModel loginModel) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, loginModel.getUserName());
        SysUser user = userService.getOne(queryWrapper);

        if(user == null){
            throw new ServiceException("用户名或密码不正确");
        }

        // 校验密码
        String pwd = MD5Util.md5Hash(loginModel.getPassword() + user.getSalt());
        if(!StringUtils.equalsAnyIgnoreCase(pwd, user.getPassword())){
            throw new ServiceException("用户名或密码不正确");
        }

        // 校验状态
        if(user.getStatus().equals(0)){
            throw new ServiceException("用户已停用");
        }

        // 更新登录时间
        updateLoginTime(user);

        UserInfo userInfo = BeanUtils.copyProperties(user, UserInfo.class);
        userInfo.setUserId(user.getUserId());

        String token = JwtUtil.sign(userInfo.getUserName(), ApplicationConfig.getSecret());

        // 缓存
        TokenUtils.saveUserInfo(userInfo);
        return token;
    }

    // 更新登录时间
    private void updateLoginTime(SysUser user){
        // 更新登录时间
        user.setLoginDate(new Date());
        // 登录ip
        user.setLoginIp(IpUtils.getIpAddress(ServletUtils.getRequest()));


        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, user.getUserId())
                        .set(SysUser::getLoginIp, user.getLoginIp())
                        .set(SysUser::getLoginDate, user.getLoginDate());

        userService.update(updateWrapper);

    }
}
