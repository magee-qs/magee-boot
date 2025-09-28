package com.magee.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.magee.common.annotation.DictField;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole extends BaseEntity {
    /**
     * 
     */
    @TableId
    private Long roleId;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String name;

    /**
     * 权限字符串
     */
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;

    /**
     * 排序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 数据范围 1： 全部 2 ：自定义 3 ：本部门 4 本部门及下属 5 本人数据
     */
    @NotNull(message = "数据范围不能为空")
    @DictField(dictType = "sys_data_scope")
    private Integer dataScope;


    /**
     * 状态
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 
     */
    private Integer deleted;


    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}