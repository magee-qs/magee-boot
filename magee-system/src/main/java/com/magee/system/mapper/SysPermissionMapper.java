package com.magee.system.mapper;

import com.magee.system.domain.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author magee
* @description 针对表【sys_permission(权限配置)】的数据库操作Mapper
* @Entity com.magee.system.domain.SysPermission
*/
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 查询用户权限
     * */
    List<String> selectUserPermission(@Param("userId")Long userId);


}




