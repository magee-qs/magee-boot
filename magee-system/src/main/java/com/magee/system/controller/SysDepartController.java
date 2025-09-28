package com.magee.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.SysDepart;
import com.magee.system.service.ISysDepartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/sys/depart")
@Api(tags = "部门管理")
public class SysDepartController {

    @Autowired
    private ISysDepartService departService;

    @PostMapping("/add")
    @ApiOperation(value = "添加部门")
    public AjaxResult add(@Validated @RequestBody SysDepart depart){
        departService.addDepart(depart);
        return AjaxResult.ok();
    }


    @GetMapping("/list")
    @ApiOperation("部门列表")
    public AjaxResult list(SysDepart sysDepart){
        QueryWrapper<SysDepart> queryWrapper = SimpleQuery.toQueryWrapper(sysDepart);

        List<SysDepart> list = departService.list(queryWrapper);
        return AjaxResult.ok(list);
    }

    @ApiOperation("查询获取部门信息")
    @GetMapping("/{departId}")
    public AjaxResult getDepart(@PathVariable  Long departId){
        SysDepart depart = departService.getById(departId);
        return AjaxResult.ok(depart);
    }

    @PostMapping("/update")
    @ApiOperation("修改部门")
    public AjaxResult update(@Validated @RequestBody SysDepart depart){
        departService.updateDepart(depart);
        return AjaxResult.ok();
    }
}
