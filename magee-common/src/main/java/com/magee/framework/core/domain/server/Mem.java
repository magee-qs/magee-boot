package com.magee.framework.core.domain.server;

import lombok.Data;

/**
 * 功能描述: 內存相关信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class Mem {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;


    private double usage ;
}
