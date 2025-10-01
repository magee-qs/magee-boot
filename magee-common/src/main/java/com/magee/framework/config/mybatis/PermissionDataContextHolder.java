package com.magee.framework.config.mybatis;

/**
 * 功能描述: 数据权限处理
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class PermissionDataContextHolder {

    private static final ThreadLocal<PermissionDataContext> HOLDER = new ThreadLocal<>();

    public static  void setContext(PermissionDataContext context){
        HOLDER.set(context);
    }

    public static PermissionDataContext getContext(){
        return HOLDER.get();
    }

    public static void clear(){
        HOLDER.remove();
    }
}
