package com.magee.framework.core.service;

import com.magee.framework.core.domain.SysLog;

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


}
