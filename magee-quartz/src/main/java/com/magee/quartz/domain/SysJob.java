package com.magee.quartz.domain;

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
import com.magee.common.exception.ValidateException;
import com.magee.common.utils.TreeUtils;
import com.magee.framework.core.domain.BaseEntity;
import com.magee.quartz.utils.CronUtils;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 定时任务
 * @TableName sys_menu
 * @author: magee
 * @version: v1.0
 */
@Data
@TableName(value ="sys_job")
public class SysJob extends BaseEntity  {
    @TableId
    @ExcelField(name="任务id",  width = 15)
    private Long jobId;

    @Size(min= 0, max = 100, message="任务名称长度在0-100之间")
    @ExcelField(name="任务名称",  width = 15)
    private String jobName;

    @Size(min= 0, max = 100, message="任务组长度在0-100之间")
    @ExcelField(name="任务组",  width = 15)
    private String jobGroup;

    @Size(min= 0, max = 100, message="任务类长度在0-100之间")
    @ExcelField(name="任务类",  width = 15)
    private String clazz;

    @Size(min= 0, max = 100, message="执行方法长度在0-100之间")
    @ExcelField(name="执行方法",  width = 15)
    private String method;

    @Size(min= 0, max = 1000, message="参数长度在0-1,000之间")
    @ExcelField(name="参数",  width = 15)
    private String param;

    @Size(min= 0, max = 255, message="表达式长度在0-255之间")
    @ExcelField(name="表达式",  width = 15)
    private String cron;


    @NotNull(message = "执行策略不能为空")
    @ExcelField(name="策略 1 立刻执行 2 执行一次 3 放弃",  width = 15)
    private Integer policy;

    @ExcelField(name="状态",  width = 15)
    @DictField(dictType = "sys_normal_disable")
    private Integer status;

    @JsonIgnore
    private Integer deleted;

    /**
     * 运行状态 0 未启动 1 运行中 2 暂停
     * */
    private Integer running;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void valid(){
        if(!CronUtils.isValid(cron)){
            throw new ValidateException("cron表达式不正确");
        }
    }
}