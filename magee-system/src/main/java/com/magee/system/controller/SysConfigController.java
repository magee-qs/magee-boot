package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysConfig;
import com.magee.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 系统配置
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Api(tags = "系统参数配置")
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private ISysConfigService configService;

    @ApiOperation("参数配置列表")
    @GetMapping("/list")
    @RequiresPermissions("sys:config:query")
    public AjaxResult list(SysConfig sysConfig) {
        QueryWrapper<SysConfig> queryWrapper = SimpleQuery.toQueryWrapper(sysConfig);
        IPage<SysConfig> page = SimpleQuery.toPage();

        configService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取参数")
    @GetMapping("/{configId}")
    @RequiresPermissions("sys:config:query")
    public AjaxResult getConfig(@PathVariable Long configId) {
        SysConfig config = configService.getById(configId);
        return AjaxResult.ok(config);
    }

    @ApiOperation("添加参数")
    @PostMapping("/add")
    @RequiresPermissions("sys:config:add")
    public AjaxResult add(@Validated @RequestBody SysConfig sysConfig) {
        configService.save(sysConfig);
        return AjaxResult.ok();
    }

    @ApiOperation("添加参数")
    @PostMapping("/edit")
    @RequiresPermissions("sys:config:edit")
    public AjaxResult edit(@Validated @RequestBody SysConfig sysConfig) {
        configService.updateById(sysConfig);
        return AjaxResult.ok();
    }

    @ApiOperation("删除参数")
    @GetMapping("/remove/{configId}")
    @RequiresPermissions("sys:config:remove")
    public AjaxResult remove(@PathVariable Long configId) {
        configService.removeById(configId);
        return AjaxResult.ok();
    }


}
