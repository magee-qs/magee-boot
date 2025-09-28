package com.magee.system.controller;

import com.magee.common.config.SystemConfig;
import com.magee.common.utils.SecurityUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.TokenUtils;
import com.magee.common.utils.file.FileUploadUtils;
import com.magee.common.utils.file.FileUtils;
import com.magee.common.utils.file.MimeTypeUtils;
import com.magee.framework.core.controller.BaseController;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.framework.core.vo.UserInfo;
import com.magee.system.domain.SysUser;
import com.magee.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/profile")
@Api(tags = "个人信息")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @ApiOperation(value = "获取个人信息")
    @GetMapping(value = "/getProfile")
    public AjaxResult getProfile(){
        AjaxResult result = userService.getProfile();
        return result;
    }

    @ApiOperation("修改个人信息")
    @PostMapping("/updateProfile")
    public AjaxResult updateProfile(@Validated @RequestBody SysUser user){
        user = userService.updateProfile(user);
        return AjaxResult.ok(user);
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody Map<String, String> params){
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        userService.updatePwd(oldPassword,newPassword);
        return AjaxResult.ok();
    }

    @ApiOperation(value = "上传头像")
    @PostMapping("/avatar")
    public AjaxResult avatar(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return AjaxResult.error("没有上传文件");
        }
        String avatar = FileUploadUtils.upload(SystemConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
        //更新头像
        userService.updateAvatar(SecurityUtils.getUserId(), avatar);

        UserInfo userInfo = SecurityUtils.getUserInfo();
        String oldAvatar = userInfo.getAvatar();
        if(StringUtils.isNotEmpty(oldAvatar)){
            FileUtils.deleteFile(SystemConfig.getAvatarPath()+ FileUtils.stripPrefix(oldAvatar));
        }
        // 设置头像
        userInfo.setAvatar(avatar);
        TokenUtils.saveUserInfo(userInfo);

        return AjaxResult.ok(avatar,"");
    }
}
