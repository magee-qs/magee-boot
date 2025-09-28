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
import javax.validation.constraints.Size;

/**
 * 字典数据表
 * @TableName sys_dict_data
 */
@TableName(value ="sys_dict_data")
@Data
public class SysDictData extends BaseEntity {
    /**
     * 
     */
    @TableId
    private Long dictCode;

    /**
     * 排序
     */
    private Integer dictSort;

    /**
     * 标签
     */
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    /**
     * 键值
     */
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    /**
     * 类型
     */
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    /**
     * 样式表
     */
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /**
     * 样式属性
     */
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String listClass;

    /**
     * 默认值 0 否 1 是
     */
    private Integer isDefault;

    /**
     * 状态 0 停用 1 启用
     */
//    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    /**
     * 
     */
    private String remark;


    /**
     * 
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}