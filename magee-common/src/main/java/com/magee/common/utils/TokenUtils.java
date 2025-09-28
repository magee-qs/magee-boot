package com.magee.common.utils;

import com.magee.common.constant.CacheConstant;
import com.magee.common.constant.CommonConstant;
import com.magee.common.utils.redis.RedisUtils;
import com.magee.framework.core.vo.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * token工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class TokenUtils {
    /** 用户缓存时长 默认: 120分钟 */
    private  static final Long Expire = 7 * 24 * 60 * 60L;
    /**
     * 获取 request 里传递的 token
     * @param request
     * */
    public static String getToken(HttpServletRequest request){
        if(request == null){
            return null;
        }

        String token = request.getHeader(CommonConstant.X_Access_Token);
        if(token == null){
            token = request.getParameter("token");
        }
        return token;
    }

    /**
     * 获取 request 里传递的 token
     * */
    public static String getToken(){
        HttpServletRequest request =  ServletUtils.getRequest();
        return getToken(request);
    }

    /**
     * 获取 request 里传递的 tenantId
     * */
    public static String getTenantId(HttpServletRequest request){
        if(request == null){
            return null;
        }

        String tenantId = request.getHeader(CommonConstant.X_TenantId);
        if(tenantId == null){
            tenantId = request.getParameter(CommonConstant.X_TenantId);
        }
        return tenantId;
    }

    /**
     * 获取 request 里传递的 tenantId
     * */
    public static String getTenantId(){
        HttpServletRequest request =  ServletUtils.getRequest();
        return getTenantId(request);
    }

    /**
     * 保存用户信息到缓存
     * @param userInfo 用户信息
     * */
    public static void saveUserInfo(UserInfo userInfo){
        RedisUtils.set(CacheConstant.SYS_USERINFO_CACHE + userInfo.getUserName() , userInfo, Expire);
    }

    /**
     * 从缓存中读取用户信息
     * @param userName 用户账号
     * */
    public static UserInfo getUserInfo(String userName){
        if(StringUtils.isEmpty(userName)){
            return null;
        }
        UserInfo userInfo = (UserInfo) RedisUtils.get(CacheConstant.SYS_USERINFO_CACHE + userName);
        return userInfo;
    }

    /**
     * 获取缓存用户过期时间
     * */
    public static Long getExpire(String userName){
        return RedisUtils.getExpire(CacheConstant.SYS_USERINFO_CACHE + userName);
    }

    /**
     * 设置用户缓存过期时间
     * */
    public static void setExpire(String userName){
        RedisUtils.expire(CacheConstant.SYS_USERINFO_CACHE + userName, Expire);
    }

    /**
     * 清空登录用户
     * */
    public static void removeUserInfo(String userName){
        RedisUtils.del(CacheConstant.SYS_USERINFO_CACHE + userName);
    }
}
