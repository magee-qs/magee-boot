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

/**
 * 部门
 * @TableName sys_depart
 */
@TableName(value ="sys_depart")
@Data
public class SysDepart extends BaseEntity {
    /**
     * 
     */
    @TableId
    private Long departId;

    /**
     * 上级
     */
    private Long parentId;

    /**
     * 组级列表
     */
    private String ancestors;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 状态
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 
     */
    private Integer deleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}