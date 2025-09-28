package com.magee.framework.core.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magee.common.annotation.SensitiveField;
import com.magee.common.sensitization.SensitiveType;
import com.magee.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 在线用户信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账户
     * */
    private String userName;

    /**
     * 用户名
     * */
    private String realName;

    /**
     * 头像
     * */
    private String avatar;

    /**
     * 用户类型 00 普通用户  99 管理员
     * */

    private String userType;

    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;


    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;
    /**
     * 当前登录角色code
     */
    private List<String> roles;
    /**
     * 当前用户权限
     * */
    private List<String> permissions;


    private static final String AdminType = "99";

    @JsonIgnore
    public boolean isAdmin(){
        return   UserInfo.isAdmin(userType);
    }

    /**
     * 判断管理员权限
     * */
    public static boolean isAdmin(String userType){
        return AdminType.equals(userType);
    }
}
