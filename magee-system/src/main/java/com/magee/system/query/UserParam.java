package com.magee.system.query;

import com.magee.framework.core.domain.PageParam;
import lombok.Data;

import java.util.Date;


/**
 * 功能描述: 用户查询
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class UserParam extends PageParam {
    /**
     * 部门id
     */
    private Long departId;

    /**
     * 账号
     */
    private String userName;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 类型 00 普通 99 admin
     */
    private String userType;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建日期
     * */
    private Date createTime;

    /**
     * 角色id
     * */
    private Long roleId;

}
