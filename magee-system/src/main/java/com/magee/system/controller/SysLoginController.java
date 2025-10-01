package com.magee.system.controller;

import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.code.kaptcha.Producer;
import com.magee.common.constant.CacheConstant;
import com.magee.common.exception.ServiceException;
import com.magee.common.utils.*;
import com.magee.common.utils.redis.RedisUtils;
import com.magee.common.utils.uuid.IdUtils;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.framework.core.vo.UserInfo;
import com.magee.system.domain.SysDictData;
import com.magee.system.domain.SysMenu;
import com.magee.system.model.LoginModel;
import com.magee.system.service.ILoginService;
import com.magee.system.service.IPermissionService;
import com.magee.system.service.ISysDictDataService;
import com.magee.system.service.ISysMenuService;
import com.magee.system.vo.RouteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
public class SysLoginController {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private IPermissionService permissionService;



    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody LoginModel loginModel){
        // 校验验证码
        String verifyKey =  CacheConstant.SYS_CAPTCHA_CODE_KEY  + loginModel.getCheckKey();
        String checkCode = (String) RedisUtils.get(verifyKey);
//        if(StringUtils.isEmpty(checkCode)){
//            throw new ServiceException("验证码已过期");
//        }
//        if(!StringUtils.equalsAnyIgnoreCase(checkCode, loginModel.getCheckCode())){
//            throw new ServiceException("验证码不正确");
//        }
        String token =  loginService.login(loginModel);
        return AjaxResult.ok(token,"");
    }


    @ApiOperation("验证码")
    @GetMapping("/captcha")
    public AjaxResult getCaptcha(){
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstant.SYS_CAPTCHA_CODE_KEY  + uuid;

        String  code = RandomUtils.randomString(4);
        BufferedImage image = null;

        String capText = captchaProducerMath.createText();
        image = captchaProducerMath.createImage(code);

        // 缓存2分钟
        RedisUtils.set(verifyKey, code , 2  * 60 );

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        AjaxResult result = AjaxResult.ok("");
        result.put("uuid", uuid);
        result.put("img", Base64.encode(os.toByteArray()));
        return result;
    }

    @ApiOperation("获取路由信息")
    @GetMapping("/getRouters")
    public AjaxResult getRouters(){
        UserInfo userInfo = SecurityUtils.getUserInfo();
        List<RouteVO> routes = menuService.getMenuTreeByUserId(userInfo.getUserId());
        return AjaxResult.ok(routes);
    }

    /**
     * 获取到用户信息
     * */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    public AjaxResult getInfo(){
        UserInfo userInfo = SecurityUtils.getUserInfo();
        AjaxResult result = AjaxResult.ok();
        result.put("userInfo", userInfo);
        result.put("roles",  permissionService.getLoginRole());
        result.put("permissions", permissionService.getLoginPermission());
        return result;
    }


    @ApiOperation("退出")
    @GetMapping("/logout")
    public AjaxResult logout(){
        String userName = SecurityUtils.getUserName();
        TokenUtils.removeUserInfo(userName);
        return AjaxResult.ok();
    }
}
