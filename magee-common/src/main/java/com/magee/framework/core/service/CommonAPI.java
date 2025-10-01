package com.magee.framework.core.service;

import com.magee.framework.core.domain.SysLog;

import java.util.List;

/**
 * 功能描述: 通用数据接口
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public interface CommonAPI {
    /**
     * 获取部门
     * */
    String getDepartName(Long departId);

    /**
     * 保存日志
     * */
    void addLog(SysLog sysLog);

    /**
     * 获取字典数据
     * */
    String getDictText(String dictValue, String dictType);

    /**
     * 根据组件名获取数据权限规则
     * */
    String getMenuDataScope(String component);

    /**
     * 获取部门的所有父级
     * */
    List<Long> getDepartAncestors(List<Long> departIds);

    /**
     * 获取部门的所有子部门
     * */
    List<Long> getDepartChildren(List<Long> departIds);
}
