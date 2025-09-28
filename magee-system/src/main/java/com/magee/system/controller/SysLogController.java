package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.domain.SysLog;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 功能描述: 系统日志
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/log")
@Api(tags = "系统日志")
public class SysLogController {

    @Autowired
    private ISysLogService logService;

    @ApiOperation("日志列表")
    @GetMapping("/list")
    @RequiresPermissions("sys:log:query")
    public AjaxResult list(SysLog sysLog){
        QueryWrapper<SysLog> queryWrapper = SimpleQuery.toQueryWrapper(sysLog);
        IPage<SysLog> page = SimpleQuery.toPage();

        logService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }


    @GetMapping("/remove/{ids}")
    @ApiOperation("删除日志")
    @RequiresPermissions("sys:log:remove")
    public AjaxResult remove(@PathVariable Long[] ids){
        logService.removeByIds(Arrays.asList(ids));
        return AjaxResult.ok();
    }
}
