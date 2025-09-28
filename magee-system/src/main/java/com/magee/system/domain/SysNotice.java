package com.magee.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.magee.common.annotation.DictField;
import com.magee.common.xss.Xss;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通告
 * @TableName sys_notice
 */
@TableName(value ="sys_notice")
@Data
public class SysNotice extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long noticeId;

    /**
     * 标题
     */
    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String title;

    /**
     * 类型 1 通知 2 通告
     */
    @DictField(dictType = "sys_notice_type")
    private Integer noticeType;

    /**
     * 状态
     */
    @DictField(dictType = "sys_normal_disable")
    private Integer status;


    /**
     * 备注
     */
    private String remark;


    @TableField(exist = false)
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}