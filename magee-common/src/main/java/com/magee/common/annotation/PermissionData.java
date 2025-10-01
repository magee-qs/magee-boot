package com.magee.common.annotation;

import java.lang.annotation.*;

/**
 * 功能描述: 数据权限过滤
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionData {
    /**
     * 菜单组件
     * 根据组件定位数据权限配置
     * 例如 "system/user/index"
     * */
    String component() default "";

    /**
     * 自定义sql
     * */
    String sql() default "";
}
