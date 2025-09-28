package com.magee.system.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 功能描述: 权限操作表单
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class PermissionModel {
    @NotNull(message = "实体参数不能为空")
    public Long itemId;

    @NotNull(message = "类型不能为空")
    public String itemType;

    @NotNull(message = "权限不能为空")
    public Long[] permIds;
}
