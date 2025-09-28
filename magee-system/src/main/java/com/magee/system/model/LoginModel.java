package com.magee.system.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 功能描述: 登录表单
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class LoginModel {
    @NotEmpty( message =  "账号不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String checkCode;

    @NotEmpty(message = "checkKey不能为空")
    private String checkKey;
}
