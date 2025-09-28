package com.magee.common.constant;

/**
 * 通用常量信息
 * */
public class CommonConstant {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * www主域
     */
    public static final String WWW = "www.";


    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";



    /**
     * token请求参数
     * */
    public static final String X_Access_Token = "X-Access-Token";

    /**
     * 租户请求参数
     * */
    public static final String X_TenantId = "X-TenantId";


    /** 请求响应状态码 */
    /* ok */
    public static final Integer SC_OK = 200;
    public static final Integer SC_ERROR = 500;
    public static final Integer SC_NOTFOUND = 404;
    public static final Integer SC_NOAUTH = 401;
    public static final Integer SC_NOPERM = 403;
    /** 请求响应状态码 */


    /**POST请求*/
    public static  final  String HTTP_POST = "POST";

    /**PUT请求*/
    public static  final  String HTTP_PUT = "PUT";

    /**PATCH请求*/
    public static  final  String HTTP_PATCH = "PATCH";

    /**未知的*/
    public static  final  String UNKNOWN = "unknown";


}
