package com.magee.system.vo;

import com.magee.common.utils.StringUtils;
import lombok.Data;

/**
 * 功能描述: 路由显示信息
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class MetaVO {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    public MetaVO(String title, String icon, boolean noCache, String link)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.isHttp(link))
        {
            this.link = link;
        }
    }

    public MetaVO(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVO(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }
}
