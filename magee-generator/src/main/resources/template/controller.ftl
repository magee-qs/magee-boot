package ${packageName}.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.annotation.AutoLog;
import com.magee.common.enums.BusinessType;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.framework.core.vo.AjaxResult;
import ${packageName}.domain.${entityName};
import ${packageName}.service.I${entityName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* 功能描述: ${comment}
*
* @author ${author}
* @version 1.0
*/
@RestController
@Api(tags = "${comment}")
@RequestMapping("${url}")
public class ${entityName}Controller {

    @Autowired
    private I${entityName}Service ${className}Service;

    @ApiOperation("${comment}")
    @GetMapping("/list")
    @RequiresPermissions("${perm}:query")
    @AutoLog(title = "${comment}", businessType = BusinessType.QUERY)
    public AjaxResult list(${entityName} ${className}){
        QueryWrapper<${entityName}> queryWrapper = SimpleQuery.toQueryWrapper(${className});
        IPage<${entityName}> page = SimpleQuery.toPage();
        ${className}Service.page(page, queryWrapper);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取${comment}")
    @GetMapping("/{${keyParam}}")
    @RequiresPermissions("${perm}:query")
    @AutoLog(title = "${comment}", businessType = BusinessType.QUERY)
    public AjaxResult get${entityName}(@PathVariable Long ${keyParam}){
        ${entityName} post = ${className}Service.getById(${keyParam});
        return AjaxResult.ok(post);
    }

    @ApiOperation("添加${comment}")
    @PostMapping("/add")
    @RequiresPermissions("${perm}:add")
    @AutoLog(title = "${comment}", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody ${entityName} ${className}){
        ${className}Service.save(${className});
        return AjaxResult.ok();
    }

    @ApiOperation("修改${comment}")
    @PostMapping("/update")
    @RequiresPermissions("${perm}:edit")
    @AutoLog(title = "${comment}", businessType = BusinessType.UPDATE)
    public AjaxResult update(@Validated @RequestBody ${entityName}  ${className}){
        UpdateWrapper<${entityName}> updateWrapper = SimpleQuery.toUpdateWrapper(${className});
        ${className}Service.update(updateWrapper);
        return AjaxResult.ok();
    }


    @ApiOperation("删除${comment}")
    @GetMapping("/remove/{${keyParam}}")
    @RequiresPermissions("${perm}:remove")
    @AutoLog(title = "${comment}", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable  Long ${keyParam}){
        ${className}Service.removeById(${keyParam});
        return AjaxResult.ok();
    }

    @ApiOperation("删除${comment}")
    @GetMapping("/status/{${keyParam}}")
    @RequiresPermissions("${perm}:edit")
    @AutoLog(title = "${comment}", businessType = BusinessType.UPDATE)
    public AjaxResult status(@PathVariable Long ${keyParam}){
        LambdaUpdateWrapper<${entityName}> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(${entityName}::get${keyField}, ${keyParam})
        .setSql("status = 1 - status");

        ${className}Service.update(updateWrapper);
        return AjaxResult.ok();
    }

}
