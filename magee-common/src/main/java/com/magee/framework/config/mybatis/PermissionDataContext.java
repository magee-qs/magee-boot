package com.magee.framework.config.mybatis;

import com.magee.common.annotation.PermissionData;
import lombok.Data;

import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class PermissionDataContext {
    /**
     * 用户id
     * */
    private Long userId;

    /**
     * 用户归属部门
     * */
    private  Long departId;

    /**
     * 用户归属部门和所有下属部门
     * */
    private List<Long> departIds ;

    /**
     * 过滤权限
     * */
    private PermissionDataRule rule;

    /**
     * 自定义sql
     * */
    private String customSql;


    /**
     * 默认depart_id字段 多表连接查询 d1.depart_id
     * */
    private  String departColumn  = "depart_id";

    /**
     * 默认user字段 多表连接查询 u1.create_by
     * */
    private String userColumn = "create_by";

}
