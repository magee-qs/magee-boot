package com.magee.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magee.common.annotation.DictField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 功能描述: 用户表单
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class UserModel {
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
     * 状态 0 停用 1 启用
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;


    /**
     *
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
