package com.magee.system.query;

import lombok.Data;

import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class RoleParam {

    private Long roleId;

    private String name;

    private String roleKey;

    private Integer dataScope;

    private Integer status;

    private Date createTime;

    private Long userId;
}
