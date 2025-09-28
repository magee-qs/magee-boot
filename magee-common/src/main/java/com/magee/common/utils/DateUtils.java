package com.magee.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 时间工具类
 * @author magee
 * @version 1.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 日期路径 年/月/日 2020/10/16
     * */
    public static String datePath(){
        Date date = new Date();
        return DateFormatUtils.format(date, "yyyy/MM/dd");
    }
    /**
     * 日期路径 年月日 20201016
     * */
    public static String dateTime(){
        Date date = new Date();
        return DateFormatUtils.format(date, "yyyyMMdd");
    }

    /**
     * 日期格式化
     * */
    public static String toStr(Date date ,String format){
        if(date == null){
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(format).format(date);
    }
}
