package com.magee.common.filter;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * 功能描述: 排除JSON敏感属性
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {
    public PropertyPreExcludeFilter()
    {
    }

    public PropertyPreExcludeFilter addExcludes(String... filters)
    {
        for (int i = 0; i < filters.length; i++)
        {
            this.getExcludes().add(filters[i]);
        }
        return this;
    }
}
