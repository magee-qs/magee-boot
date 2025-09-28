package com.magee.system.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 功能描述: 角色-分配用户
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class RoleOfUserModel {

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    private List<Long> userIds;
}
