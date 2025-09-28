package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.controller.BaseController;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysDictData;
import com.magee.system.service.ISysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 数据字典
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/dict/data")
@Api(tags = "数据字典")
public class SysDictController extends BaseController {

    @Autowired
    private ISysDictDataService dictService;

    @ApiOperation(value = "查询字典列表")
    @RequiresPermissions("system:dict:query")
    @GetMapping("/list")
    public AjaxResult list(SysDictData sysDictData){
        QueryWrapper<SysDictData> queryWrapper = SimpleQuery.toQueryWrapper(sysDictData);
        IPage<SysDictData> page = SimpleQuery.toPage();

        dictService.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation(value = "获取字典")
    @RequiresPermissions("system:dict:query")
    @GetMapping("/{dictCode}")
    public AjaxResult getDictById(@PathVariable Long dictCode ){
        SysDictData dict = dictService.getById(dictCode);
        return AjaxResult.ok(dict);
    }

    @ApiOperation(value = "添加字典")
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysDictData sysDictData){
        dictService.save(sysDictData);
        return AjaxResult.ok();
    }


    @ApiOperation(value = "修改字典")
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysDictData sysDictData){
        dictService.updateDict(sysDictData);
        return AjaxResult.ok();
    }

    @ApiOperation(value = "删除字典")
    @RequiresPermissions("system:dict:remove")
    @GetMapping("/remove/{dictId}")
    public AjaxResult remove(@PathVariable Long dictId){
        dictService.removeDict(dictId);
        return AjaxResult.ok();
    }

    @ApiOperation("修改状态")
    @RequiresPermissions("system:dict:edit")
    @GetMapping("/status/{dictCode}")
    public AjaxResult status(@PathVariable Long dictCode){
        dictService.changeStatus(dictCode);
        return  AjaxResult.ok();
    }


}
