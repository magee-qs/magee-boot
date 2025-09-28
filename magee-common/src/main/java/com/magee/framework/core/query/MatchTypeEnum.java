package com.magee.framework.core.query;

import com.magee.common.utils.StringUtils;

/**
 * 功能描述: 查询链接规则
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public enum MatchTypeEnum {
    AND("AND"),OR("OR");

    private String value;

    MatchTypeEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static MatchTypeEnum getByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(MatchTypeEnum val: values()){
            if(val.getValue().toLowerCase().equals(value.toLowerCase())){
                return val;
            }
        }
        return null;
    }
}
