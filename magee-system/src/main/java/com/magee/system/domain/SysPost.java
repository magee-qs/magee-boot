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
 * 岗位
 * @TableName sys_post
 */
@TableName(value ="sys_post")
@Data
public class SysPost  extends BaseEntity {
    /**
     * 
     */
    @TableId
    private Long postId;

    /**
     * 岗位编码
     */
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 30, message = "岗位编码长度不能超过30个字符")
    private String code;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 30, message = "岗位名称长度不能超过30个字符")
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 状态
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;


    /**
     * 状态
     * */
    private Integer deleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}