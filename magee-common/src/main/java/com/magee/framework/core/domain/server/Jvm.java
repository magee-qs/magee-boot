package com.magee.framework.core.domain.server;

import lombok.Data;

/**
 * 功能描述: JVM相关信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class Jvm {
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;


    private double used;

    private double usage;



    private String start;

    private String running;
}
