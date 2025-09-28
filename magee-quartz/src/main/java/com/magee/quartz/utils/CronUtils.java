package com.magee.quartz.utils;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * 功能描述: cron表达式工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class CronUtils {
    /**
     * 返回一个布尔值代表一个给定的Cron表达式的有效性
     *
     * @param cronExpression Cron表达式
     * @return boolean 表达式是否有效
     */
    public static boolean isValid(String cronExpression){
        return CronExpression.isValidExpression(cronExpression);
    }

}
