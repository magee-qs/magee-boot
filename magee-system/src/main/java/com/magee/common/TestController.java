package com.magee.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.annotation.PermissionData;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysUser;
import com.magee.system.query.UserParam;
import com.magee.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/test")
@Api(tags = "测试用接口")
public class TestController {

    @Autowired
    private ISysUserService userService;


    /**
     * 测试数据访问权限
     * */
    @ApiOperation("测试数据权限")
    @GetMapping("/list")
    @PermissionData(component = "system/user/index")
    public AjaxResult list(UserParam sysUser){
        QueryWrapper<SysUser> queryWrapper = SimpleQuery.toQueryWrapper(sysUser);
        IPage<SysUser> page = SimpleQuery.toPage();
        page = userService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }
}
