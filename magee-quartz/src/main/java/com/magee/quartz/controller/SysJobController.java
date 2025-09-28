package com.magee.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.annotation.AutoLog;
import com.magee.common.enums.BusinessType;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.quartz.domain.SysJob;
import com.magee.quartz.service.ISysJobService;
import com.magee.quartz.utils.QuartzUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 定时任务
 *
 * @author magee
 * @version 1.0
 */
@RestController
@Api(tags = "定时任务")
@RequestMapping("/monitor/job")
public class SysJobController {

    @Autowired
    private ISysJobService sysJobService;

    @Autowired
    private Scheduler scheduler;

    @ApiOperation("查询定时任务")
    @GetMapping("/list")
    @RequiresPermissions("monitor:job:query")
    @AutoLog(title = "定时任务", businessType = BusinessType.QUERY)
    public AjaxResult list(SysJob sysJob){
        QueryWrapper<SysJob> queryWrapper = SimpleQuery.toQueryWrapper(sysJob);
        IPage<SysJob> page = SimpleQuery.toPage();
        sysJobService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取定时任务")
    @GetMapping("/{jobId}")
    @RequiresPermissions("monitor:job:query")
    @AutoLog(title = "定时任务", businessType = BusinessType.QUERY)
    public AjaxResult getSysJob(@PathVariable Long jobId){
        SysJob post = sysJobService.getById(jobId);
        return AjaxResult.ok(post);
    }

    @ApiOperation("添加定时任务")
    @PostMapping("/add")
    @RequiresPermissions("monitor:job:add")
    @AutoLog(title = "定时任务", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysJob sysJob){
        sysJob.valid();
        sysJobService.save(sysJob);
        return AjaxResult.ok();
    }

    @ApiOperation("修改定时任务")
    @PostMapping("/update")
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    public AjaxResult update(@Validated @RequestBody SysJob  sysJob){
        sysJob.valid();
        UpdateWrapper<SysJob> updateWrapper = SimpleQuery.toUpdateWrapper(sysJob);
        sysJobService.update(updateWrapper);
        return AjaxResult.ok();
    }


    @ApiOperation("删除定时任务")
    @GetMapping("/remove/{jobId}")
    @RequiresPermissions("monitor:job:remove")
    @AutoLog(title = "定时任务", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable  Long jobId){
        sysJobService.removeById(jobId);
        return AjaxResult.ok();
    }

    @ApiOperation("修改定时任务")
    @GetMapping("/status/{jobId}")
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    public AjaxResult status(@PathVariable Long jobId){
        LambdaUpdateWrapper<SysJob> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysJob::getJobId, jobId)
                .setSql("status = 1 - status");

        sysJobService.update(updateWrapper);
        return AjaxResult.ok();
    }

    /**
     * 启动任务
     */
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @GetMapping("/startJob/{jobId}")
    public AjaxResult startJob(@PathVariable Long jobId) throws SchedulerException {
        SysJob job = sysJobService.getById(jobId);
        if(job == null){
            return AjaxResult.error("任务不存在");
        }
        QuartzUtils.startJob(job, scheduler);
        // 启动状态
        sysJobService.changeRunningState(jobId, 1);
        return AjaxResult.ok();
    }

    /**
     * 执行任务一次
     */
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @GetMapping("/runJob/{jobId}")
    public AjaxResult runJob(@PathVariable Long jobId) throws SchedulerException {
        SysJob job = sysJobService.getById(jobId);
        if(job == null){
            return AjaxResult.error("任务不存在");
        }
        QuartzUtils.runJob(job, scheduler);
        return AjaxResult.ok();
    }

    /**
     * 暂停任务
     */
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @GetMapping("/pauseJob/{jobId}")
    public AjaxResult pauseJob(@PathVariable Long jobId) throws SchedulerException {
        SysJob sysJob = sysJobService.getById(jobId);
        if(sysJob == null){
            return AjaxResult.error("任务不存在");
        }
        QuartzUtils.pauseJob(sysJob, scheduler);
        // 暂停状态
        sysJobService.changeRunningState(jobId, 2);
        return AjaxResult.ok();
    }

    /**
     * 重启任务
     */
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @GetMapping("/resumeJob/{jobId}")
    public AjaxResult resumeJob(@PathVariable Long jobId) throws SchedulerException {
        SysJob job = sysJobService.getById(jobId);
        if(job == null){
            return AjaxResult.error("任务不存在");
        }
        QuartzUtils.resumeJob(job, scheduler);
        // 恢复运行
        sysJobService.changeRunningState(jobId, 1);
        return AjaxResult.ok();
    }


    /**
     * 删除任务
     */
    @RequiresPermissions("monitor:job:edit")
    @AutoLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @GetMapping("/removeJob/{jobId}")
    public AjaxResult removeJob(@PathVariable Long jobId) throws SchedulerException {
        SysJob job = sysJobService.getById(jobId);
        if(job == null){
            return AjaxResult.error("任务不存在");
        }

        QuartzUtils.removeJob(job, scheduler);
        // 删除定时任务
        sysJobService.changeRunningState(jobId, 0);
        return AjaxResult.ok();
    }

}
