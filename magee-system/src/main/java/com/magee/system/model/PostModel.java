package com.magee.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.magee.common.annotation.DictField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 功能描述: 岗位表单
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class PostModel implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
