package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysNotice;
import com.magee.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 通告管理
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@Api(tags = "通告管理")
@RequestMapping("/sys/notice")
public class SysNoticeController {

    @Autowired
    private ISysNoticeService noticeService;

    @GetMapping("/list")
    @ApiOperation("通告列表")
    @RequiresPermissions("sys:notice:query")
    public AjaxResult list(SysNotice notice){
        QueryWrapper<SysNotice> queryWrapper = SimpleQuery.toQueryWrapper(notice);
        IPage<SysNotice> page = SimpleQuery.toPage();
        noticeService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @GetMapping("/{noticeId}")
    @RequiresPermissions("sys:notice:query")
    @ApiOperation("通告列表")
    public AjaxResult getNotice(@PathVariable Long noticeId){
        SysNotice notice = noticeService.getById(noticeId);
        return AjaxResult.ok(notice);
    }

    @GetMapping("/content/{noticeId}")
    @RequiresPermissions("sys:notice:query")
    @ApiOperation("获取通告内容")
    public AjaxResult getContentById(@PathVariable Long noticeId){
       String content = noticeService.getContentById(noticeId);
       return AjaxResult.ok(content,""  );
    }


    @PostMapping("/add")
    @RequiresPermissions("sys:notice:add")
    @ApiOperation("添加通告")
    public AjaxResult add(@Validated @RequestBody SysNotice notice){
        noticeService.addNotice(notice);
        return AjaxResult.ok(  );
    }

    @PostMapping("/edit")
    @RequiresPermissions("sys:notice:edit")
    @ApiOperation("修改")
    public AjaxResult edit(@Validated @RequestBody SysNotice notice){
        noticeService.editNotice(notice);
        return AjaxResult.ok(  );
    }

    @GetMapping("/remove/{noticeId}")
    @RequiresPermissions("sys:notice:remove")
    @ApiOperation("删除通告")
    public AjaxResult remove(@PathVariable Long noticeId){
        noticeService.removeById(noticeId);
        return AjaxResult.ok();
    }

    @GetMapping("/status/{noticeId}")
    @RequiresPermissions("sys:notice:remove")
    @ApiOperation("删除通告")
    public AjaxResult status(@PathVariable Long noticeId){
        LambdaUpdateWrapper<SysNotice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysNotice::getNoticeId, noticeId)
                .setSql("status = 1 - status");

        noticeService.update(updateWrapper);
        return AjaxResult.ok();
    }

}
