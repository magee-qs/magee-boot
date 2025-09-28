package com.magee.generator.controller;

import com.magee.framework.core.vo.AjaxResult;
import com.magee.generator.service.IGenTableService;
import com.magee.generator.util.GenUtils;
import com.magee.generator.vo.ColumnVO;
import com.magee.generator.vo.FormModel;
import com.magee.generator.vo.TableVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@RestController
@RequestMapping("/tool/gen")
@Api(tags = "代码生成")
@Slf4j
public class GenController {

    @Autowired
    private IGenTableService genTableService;
    /**
     * 数据表
     * */
    @ApiOperation("查询表")
    @GetMapping("/list")
    @RequiresPermissions("sys:gen:query")
    public AjaxResult list()   {
        List<TableVO> tables = genTableService.selectTable();
        return AjaxResult.ok(tables);
    }

    @ApiOperation("生成代码")
    @RequiresPermissions("sys:gen:query")
    @PostMapping("/generate")
    public AjaxResult generate(@Validated @RequestBody FormModel formModel){
        List<ColumnVO> columns = genTableService.selectColumn(formModel.getName());

        //生成代码
        Map<String, String> map = GenUtils.generator(formModel, columns);
        return AjaxResult.ok( map );
    }
}
