package com.magee.common.utils;

import com.magee.common.utils.bean.BeanUtils;
import com.magee.common.utils.encrypt.EncryptUtils;
import com.magee.common.utils.encrypt.MD5Util;
import com.magee.framework.core.vo.UserInfo;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * shiro鉴权操作
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class SecurityUtils {
    /**
     *  当前操作用户信息 Subject
     * */
    public static Subject getSubject(){
        return org.apache.shiro.SecurityUtils.getSubject();
    }

    /**
     * 获取当前用户信息
     * */
    public static UserInfo getUserInfo(){
        UserInfo userInfo = null;
        try{
            Subject subject = getSubject();
            Object obj = subject.getPrincipal();
            if(StringUtils.isNotNull(obj)){
                userInfo = new UserInfo();
                BeanUtils.copyProperties(obj, userInfo);
            }
        }catch (Exception e){
            return null;
        }
        return userInfo;
    }

    public static Long getUserId()
    {
        UserInfo userInfo = getUserInfo();
        return userInfo == null ? null : userInfo.getUserId();
    }

    public static String getUserName(){
        UserInfo userInfo = getUserInfo();
        return userInfo == null ? null : userInfo.getUserName();
    }

    /**
     * 返回第一个部门
     * */
    public static Long getDepartId(){
        UserInfo userInfo = getUserInfo();
        return  userInfo == null ? null : userInfo.getDepartId();
    }

    /**
     * 返回所有部门
     * */
    public static List<Long> getDepartIds(){
        UserInfo userInfo = getUserInfo();
        return userInfo == null ? null : userInfo.getDepartIds();
    }

    public static String encryptPwd(String pwd, String salt){
        return MD5Util.md5Hash(pwd + salt);
    }
}
