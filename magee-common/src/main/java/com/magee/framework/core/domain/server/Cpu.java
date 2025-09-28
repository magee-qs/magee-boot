package com.magee.framework.core.domain.server;

import lombok.Data;

/**
 * 功能描述: CPU相关信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class Cpu {
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;
}
