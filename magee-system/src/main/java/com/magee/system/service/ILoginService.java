package com.magee.system.service;

import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.model.LoginModel;

/**
 * 功能描述: 用户登录
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public interface ILoginService {
    /**
     * 用户登录
     * @param loginModel 登录表单
     * @return token
     * */
    String  login(LoginModel loginModel);
}
