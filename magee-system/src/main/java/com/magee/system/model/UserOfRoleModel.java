package com.magee.system.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class UserOfRoleModel {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    private List<Long> roleIds;
}
