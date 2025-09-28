package com.magee.framework.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.magee.common.annotation.DictField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * @TableName sys_log
 */
@TableName(value ="sys_log")
@Data
public class SysLog implements Serializable {
    /**
     * 
     */
    @TableId
    private Long logId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型
     */

    private Integer businessType;

    /**
     * 方法名
     */
    private String controller;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 操作类别 
     */
    @DictField(dictType = "sys_oper_type")
    private Integer operateType;

    /**
     * 操作人
     */
    private String operateName;

    /**
     * 操作部门
     */
    private String operateDepart;

    /**
     * 主机地址
     */
    private String operateIp;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态
     */
    @DictField(dictType = "sys_common_status")
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作用时
     */
    private Long costTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}