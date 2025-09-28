package com.magee.system.enums;

/**
 * 功能描述: 权限分组
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public enum PermissionItemType {
    /** 角色 */
    Role(1),
    /** 用户 */
    User(2),
    /** 用户组 */
    UserGroup(3),
    /** 部门 */
    Depart(4);

    private Integer value;

    PermissionItemType(Integer itemType){
        this.value = itemType;
    }

    public Integer getValue(){
        return value;
    }
}
