package com.magee.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.annotation.AutoLog;
import com.magee.common.enums.BusinessType;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.quartz.domain.SysJobLog;
import com.magee.quartz.service.ISysJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 定时任务日志
 *
 * @author magee
 * @version 1.0
 */
@RestController
@Api(tags = "定时任务日志")
@RequestMapping("/monitor/jobLog")
public class SysJobLogController {

    @Autowired
    private ISysJobLogService sysJobLogService;

    @ApiOperation("定时任务日志")
    @GetMapping("/list")
    @RequiresPermissions("monitor:jobLog:query")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.QUERY)
    public AjaxResult list(SysJobLog sysJobLog){
        QueryWrapper<SysJobLog> queryWrapper = SimpleQuery.toQueryWrapper(sysJobLog);
        IPage<SysJobLog> page = SimpleQuery.toPage();
        sysJobLogService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取定时任务日志")
    @GetMapping("/{logId}")
    @RequiresPermissions("monitor:jobLog:query")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.QUERY)
    public AjaxResult getSysJobLog(@PathVariable Long logId){
        SysJobLog post = sysJobLogService.getById(logId);
        return AjaxResult.ok(post);
    }

    @ApiOperation("添加定时任务日志")
    @PostMapping("/add")
    @RequiresPermissions("monitor:jobLog:add")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysJobLog sysJobLog){
        sysJobLogService.save(sysJobLog);
        return AjaxResult.ok();
    }

    @ApiOperation("修改定时任务日志")
    @PostMapping("/update")
    @RequiresPermissions("monitor:jobLog:edit")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@Validated @RequestBody SysJobLog  sysJobLog){
        UpdateWrapper<SysJobLog> updateWrapper = SimpleQuery.toUpdateWrapper(sysJobLog);
        sysJobLogService.update(updateWrapper);
        return AjaxResult.ok();
    }


    @ApiOperation("删除定时任务日志")
    @GetMapping("/remove/{logId}")
    @RequiresPermissions("monitor:jobLog:remove")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable  Long logId){
        sysJobLogService.removeById(logId);
        return AjaxResult.ok();
    }

    @ApiOperation("删除定时任务日志")
    @GetMapping("/status/{logId}")
    @RequiresPermissions("monitor:jobLog:edit")
    @AutoLog(title = "定时任务日志", businessType = BusinessType.UPDATE)
    public AjaxResult status(@PathVariable Long logId){
        LambdaUpdateWrapper<SysJobLog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysJobLog::getLogId, logId)
                .setSql("status = 1 - status");

        sysJobLogService.update(updateWrapper);
        return AjaxResult.ok();
    }

}
