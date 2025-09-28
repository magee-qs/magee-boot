package com.magee.framework.core.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magee.common.utils.ObjectUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述: 查询字段
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
@Slf4j
public class QueryField {

    private QueryRuleEnum rule;

    private Object value;

    private String name;

    private String column;

    /**
     * @param column 数据列名
     * @param name 字段名
     * @param value 字段值
     * @param rule  查询规则
     * */
    public QueryField(String column,String name, Object value, QueryRuleEnum rule){
        this.column = column;
        this.name = name;
        this.value = value;
        this.rule = rule;
    }

    /**
     * 执行查询
     * */
    public void doQuery(QueryWrapper<?> queryWrapper){
        if(value == null || rule == null || ObjectUtils.isEmpty((value)))
            return;

        switch (rule){
            case GT:
                queryWrapper.gt(column, value);
                break;
            case GE:
                queryWrapper.ge(column,value);
                break;
            case LT:
                queryWrapper.lt(column,value);
                break;
            case LE:
                queryWrapper.le(column,value);
                break;
            case EQ:
                queryWrapper.eq(column,value);
                break;
            case NE:
                queryWrapper.ne(column,value);
                break;
            case IN:
                if(value instanceof String){
                    // 字符串
                    queryWrapper.in(column, (Object[])value.toString().split(QueryConstant.Split));
                }else if(value instanceof String[]){
                    // 字符数组
                    queryWrapper.in(column,  (Object[])value);
                }else{
                    // 数值类型
                    queryWrapper.in(column, value);
                }
                break;
            case LIKE:
                queryWrapper.like(column, value);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(column,value);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(column,value);
                break;
            default:
                log.info("======查询规则未匹配======");
                break;
        }
    }
}
