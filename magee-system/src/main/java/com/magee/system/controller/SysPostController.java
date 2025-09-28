package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysPost;
import com.magee.system.model.PostModel;
import com.magee.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 岗位
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@Api(tags = "岗位管理")
@RequestMapping("/sys/post")
public class SysPostController {

    @Autowired
    private ISysPostService postService;

    @ApiOperation("岗位列表")
    @GetMapping("/list")
    @RequiresPermissions("sys:post:query")
    public AjaxResult list(SysPost post){
        QueryWrapper<SysPost> queryWrapper = SimpleQuery.toQueryWrapper(post);
        IPage<SysPost> page = SimpleQuery.toPage();
        postService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取岗位")
    @GetMapping("/{postId}")
    @RequiresPermissions("sys:post:query")
    public AjaxResult getPost(@PathVariable Long postId){
        SysPost post = postService.getById(postId);
        return AjaxResult.ok(post);
    }

    @ApiOperation("添加岗位")
    @PostMapping("/add")
    @RequiresPermissions("sys:post:add")
    public AjaxResult add(@Validated @RequestBody SysPost sysPost){
        postService.save(sysPost);
        return AjaxResult.ok();
    }

    @ApiOperation("修改岗位")
    @PostMapping("/update")
    @RequiresPermissions("sys:post:edit")
    public AjaxResult update(@Validated @RequestBody PostModel postModel){
        UpdateWrapper<SysPost> updateWrapper = SimpleQuery.toUpdateWrapper(postModel);
        postService.update(updateWrapper);
        return AjaxResult.ok();
    }


    @ApiOperation("删除岗位")
    @GetMapping("/remove/{postId}")
    @RequiresPermissions("sys:post:remove")
    public AjaxResult remove(@PathVariable  Long postId){
        postService.removeById(postId);
        return AjaxResult.ok();
    }

    @ApiOperation("删除岗位")
    @GetMapping("/status/{postId}")
    @RequiresPermissions("sys:post:remove")
    public AjaxResult status(@PathVariable Long postId){
        LambdaUpdateWrapper<SysPost> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysPost::getPostId, postId)
                .setSql("status = 1 - status");

        postService.update(updateWrapper);
        return AjaxResult.ok();
    }
}
