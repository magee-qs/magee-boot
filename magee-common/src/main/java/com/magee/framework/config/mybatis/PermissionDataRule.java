package com.magee.framework.config.mybatis;

/**
 * 功能描述: 权限规则
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public enum PermissionDataRule {
    /**
     * 所有数据
     * */
    All(1),
    /**
     * 自定义数据权限
     * */
    Custom(2),
    /**
     * 本部门数据
     * */
    Depart(3),
    /**
     * 本部门及以下数据
     * */
    AllDepart(4),
    /**
     * 仅本人数据
     * */
    User(5);

    private Integer value;

    PermissionDataRule(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }
}
