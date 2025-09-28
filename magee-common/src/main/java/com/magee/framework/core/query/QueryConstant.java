package com.magee.framework.core.query;

import java.text.SimpleDateFormat;

/**
 * 功能描述: 查询参数常量
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class QueryConstant {
    public static final String Begin = "_begin";
    public static final String End = "_end";
    public static final String Split = ",";
    // 多值查询
    public static final String Multi = "_multi";

    // 排序参数
    public static final String Orders = "orders";
    // 排序列名参数
    public static final String Column = "column";
    // 排序方式参数
    public static final String ASC = "asc";

    // 页码
    public static final String PageNum = "pageNum";
    // 记录数
    public static final String PageSize = "pageSize";

    /**时间格式化 */
    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();
    public static SimpleDateFormat getTime(){
        SimpleDateFormat time = local.get();
        if(time == null){
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            local.set(time);
        }
        return time;
    }
}
