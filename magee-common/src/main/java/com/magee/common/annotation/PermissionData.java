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

    /**
     * 默认depart_id字段 多表连接查询 d1.depart_id
     * */
    String departColumn() default  "depart_id";

    /**
     * 默认user字段 多表连接查询 u1.create_by
     * */
    String userColumn() default  "create_by";
}
