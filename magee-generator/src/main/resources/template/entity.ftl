package ${packageName}.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magee.common.annotation.DictField;
import com.magee.common.annotation.ExcelField;
import com.magee.common.utils.TreeUtils;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Size;

/**
* ${comment}
* @TableName sys_menu
* @author: ${author}
* @version: v1.0
*/
@Data
@TableName(value ="${tableName}")
public class ${entityName} extends BaseEntity  {
<#list columns as column>
    <#if column.isKey>
    @TableId
        <#elseif column.nullable ==  0 >
            <#if column.dataType == 'varchar'>
    @NotEmpty(message="${column.comment}不能为空")
        <#else>
    @NotNull(message="${column.comment}不能为空")
        </#if>
    </#if>
    <#if column.max gt 0>
    @Size(min= ${column.min}, max = ${column.max?string("0")}, message="${column.comment}长度在${column.min}-${column.max}之间")
    </#if>
    <#if column.dataType == 'datetime'>
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(name="${column.comment}", width=20, dateFormat="yyyy-MM-dd HH:mm:ss")
    <#elseif column.dataType == 'date'>
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @ExcelField(name="${column.comment}", width=15, dateFormat="yyyy-MM-dd")
    <#else>
    @ExcelField(name="${column.comment}",  width = 15)
    </#if>
    private ${column.fieldType} ${column.fieldName};

</#list>
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}