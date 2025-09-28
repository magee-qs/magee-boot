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
import com.magee.common.utils.TreeUtils;
import com.magee.framework.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 定时任务日志
 * @TableName sys_menu
 * @author: magee
 * @version: v1.0
 */
@Data
@TableName(value ="sys_job_log")
public class SysJobLog    {
    @TableId
    @ExcelField(name="日志id",  width = 15)
    private Long logId;

    @ExcelField(name="",  width = 15)
    private Long jobId;

    @Size(min= 0, max = 100, message="任务名长度在0-100之间")
    @ExcelField(name="任务名",  width = 15)
    private String jobName;

    @Size(min= 0, max = 100, message="任务分组长度在0-100之间")
    @ExcelField(name="任务分组",  width = 15)
    private String jobGroup;

    @Size(min= 0, max = 100, message="类名长度在0-100之间")
    @ExcelField(name="类名",  width = 15)
    private String clazz;

    @Size(min= 0, max = 100, message="方法名长度在0-100之间")
    @ExcelField(name="方法名",  width = 15)
    private String method;

    @Size(min= 0, max = 1000, message="参数长度在0-1,000之间")
    @ExcelField(name="参数",  width = 15)
    private String param;

    @Size(min= 0, max = 10, message="执行信息长度在0-10之间")
    @ExcelField(name="执行信息",  width = 15)
    private Boolean message;

    @ExcelField(name="执行状态",  width = 15)
    private Integer status;

    @Size(min= 0, max = 2000, message="异常信息长度在0-2,000之间")
    @ExcelField(name="异常信息",  width = 15)
    private String errorInfo;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(name="开始时间", width=20, dateFormat="yyyy-MM-dd HH:mm:ss")
    private Date start;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(name="结束时间", width=20, dateFormat="yyyy-MM-dd HH:mm:ss")
    private Date end;

    @ExcelField(name="耗时",  width = 15)
    private Long total;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}