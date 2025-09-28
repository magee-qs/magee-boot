package com.magee.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magee.common.annotation.DictField;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser  extends BaseEntity {
    /**
     * 用户id
     */
    @TableId
    private Long userId;

    /**
     * 部门id
     */
    private Long departId;

    /**
     * 账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    /**
     * 用户名
     */
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /**
     * 类型 00 普通 99 admin
     */
    @JsonIgnore
    private String userType;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 电话
     */
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phone;

    /**
     * 性别
     */
    @DictField(dictType = "sys_user_sex")
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 状态 0 停用 1 启用
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 
     */
    @JsonIgnore
    private Integer deleted;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录日期
     */
    private Date loginDate;

    /**
     * 密码更新日期
     */
    private Date pwdUpdateTime;

    /**
     * 
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}