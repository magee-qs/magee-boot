package com.magee.framework.core.query;

import com.magee.common.utils.StringUtils;
import lombok.Data;

import java.util.Map;

/**
 * 功能描述: 查询规则
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public enum QueryRuleEnum {
    GT(">","gt","大于"),
    GE(">=","ge","大于等于"),
    LT("<","lt","小于"),
    LE("<=","le","小于等于"),
    EQ("=","eq","等于"),
    NE("!=","ne","不等于"),
    IN("IN","in","包含"),
    LIKE("LIKE","like","全模糊"),
    LEFT_LIKE("LEFT_LIKE","left_like","左模糊"),
    RIGHT_LIKE("RIGHT_LIKE","right_like","右模糊"),
    SQL_RULES("USE_SQL_RULES","ext","自定义SQL片段");

    private String value;

    public String getValue() {
        return value;
    }

    public String getCondition() {
        return condition;
    }

    public String getMessage(){
        return message;
    }

    private String condition;

    private String message;

    QueryRuleEnum(String value, String condition, String message){
        this.value = value;
        this.condition = condition;
        this.message = message;
    }


    /**
     * 根据值或条件获取查询方法
     * value / condition
     * */
    public static QueryRuleEnum getByValue(String value){
        if(StringUtils.isNotEmpty(value)){
            return null;
        }
        for(QueryRuleEnum val : values()){
            if(val.getValue().equals(value) || val.getCondition().equals(value)){
                return val;
            }
        }
        return null;
    }
}
