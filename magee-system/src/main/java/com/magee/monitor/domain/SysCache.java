package com.magee.monitor.domain;

import com.magee.common.utils.StringUtils;
import lombok.Data;

/**
 * 功能描述:  缓存信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class SysCache {
    /** 缓存名称 */
    private String cacheName = "";

    /** 缓存键名 */
    private String cacheKey = "";

    /** 缓存内容 */
    private String cacheValue = "";

    /** 备注 */
    private String remark = "";

    public SysCache(){}

    public SysCache(String cacheName, String remark ){
        this.cacheName = cacheName;
        this.remark  = remark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue)  {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}
