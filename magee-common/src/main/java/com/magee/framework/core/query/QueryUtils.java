package com.magee.framework.core.query;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magee.common.utils.*;
import com.magee.common.utils.reflect.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: 查询工具操作类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class QueryUtils {

    /**
     * 构造查询条件
     * @param searchObj 查询参数
     * @param type 数据类型
     * @param name 字段名
     * @param column 列名
     * */
    public static  List<QueryField> doEasyQuery(Object searchObj, String type, String name, String column){
        String value = null;
        List<QueryField> list = new ArrayList<>();

        // 判断是否区间条件
        value = getParameterValue(name + QueryConstant.Begin) ;
        if(StringUtils.isNotEmpty(value)){
            list.add(new QueryField(column,name, value, QueryRuleEnum.GE));
        }

        value = getParameterValue(name + QueryConstant.End) ;
        if(StringUtils.isNotEmpty(value)){
            list.add( new QueryField(column,name, value,  QueryRuleEnum.LE));
        }

        // 多值查询
        value = getParameterValue(name + QueryConstant.Multi) ;
        if(StringUtils.isNotEmpty(value)){
            list.add(new QueryField(column,name, value,  QueryRuleEnum.IN));
        }

        // 获取参数值
        value = getSearchObjValue(searchObj,name);
        if(StringUtils.isNotEmpty(value)){
            // 字段查询
            list.addAll(getRuleByType(column, name,   type, value));
        }else{
            //从请求参数中获取查询值 => 字段 + 后缀查询
            list.addAll(getRuleBySuffix(column,name,type)) ;
        }
        return list;
    }

    /**
     * 根据字段名 类型 值生成查询条件
     * 日期类型 转换为区间查询
     * 字符串类型 转换为like查询
     * 其他类型 转换为 eq查询
     * @param name 字段名
     * @param column 列名
     * @param type 字段类型
     * @param value 字段值
     * */
    private static List<QueryField> getRuleByType(String column, String name, String type, String value){
        List<QueryField> list = new ArrayList<>();
        if(type.equals(Date.class.toString())){
            // 日期类型
            Object start =  getDateQueryByRule(value,QueryRuleEnum.GE);
            list.add(new QueryField(column,name,start,QueryRuleEnum.GE));

            Object end =  getDateQueryByRule(value,QueryRuleEnum.LE);
            list.add(new QueryField(column,name,end,QueryRuleEnum.LE));
        }else if(type.equals(String.class.toString())){
            // 字符串类型
            list.add(new QueryField(column,name,value,QueryRuleEnum.LIKE));
        }else{
            // 比较
            list.add(new QueryField(column,name,value,QueryRuleEnum.EQ));
        }
        return list;
    }

    /**
     * 生成查询条件
     *  @param name 字段名
     *  @param column 列名
     *  @param type 字段类型
     * */
    private static List<QueryField> getRuleBySuffix(String column ,String name  ,String type){
        List<QueryField> list = new ArrayList<>();
        for (QueryRuleEnum rule : QueryRuleEnum.values()){
            String param = name + "_" +  rule.getCondition();
            // 从请求参数或获取查询参数 例如 userName_like =>
            String value  = getParameterValue(param);

            if(StringUtils.isNotEmpty(value)){
                // 找到查询条件
                if(type.equals(Date.class.toString())){
                    // 日期类型
                    Object end =  getDateQueryByRule(value,QueryRuleEnum.LE);
                    list.add(new QueryField(column,name,end, QueryRuleEnum.LE ));

                    Object start =  getDateQueryByRule(value,QueryRuleEnum.GE);
                    list.add(new QueryField(column,name,start, QueryRuleEnum.GE ));

                }else  {
                    // 后缀查询
                    Object dbValue = ConvertUtils.toObj(type, value);
                    list.add(new QueryField(column,name,dbValue,  rule));
                }
                break;
            }
        }
        return list;
    }


    /**
     * 日期类型查询
     * */
    private static Object getDateQueryByRule(String value,  QueryRuleEnum rule){
        Date date = null;
        if(StringUtils.length(value) == 10){
            // 2025-10-12
            if(rule == QueryRuleEnum.GE){
                // 大于等于
                date = DateUtils.parseDate(value + " 00:00:00");
            }else if(rule == QueryRuleEnum.LE){
                // 小于等于
                date = DateUtils.parseDate(value + " 23:59:59");
            }
        }
        if(date == null){
            date = DateUtils.parseDate(value);
        }
        return date;
    }




    /**
     * 获取请求中的参数值
     * */
    public static String getParameterValue(String name){
        try{
            Map<String,String[]> parameterMap = ServletUtils.getRequest().getParameterMap();
            String[] arr = parameterMap.get(name);
            if(arr == null || arr.length == 0){
                return StringUtils.EMPTY;
            }
            return ConvertUtils.toStr(arr[0], "");
        }catch (Exception ex) {
            return StringUtils.EMPTY;
        }
    }



    /**
     * 获取请求中的参数值
     * */
    public static String getParameterValue(Map<String,String[]> parameterMap, String name){
        try{
            String[] arr = parameterMap.get(name);
            if(arr == null || arr.length == 0){
                return StringUtils.EMPTY;
            }
            return ConvertUtils.toStr(arr[0], "");
        }catch (Exception ex) {
            return StringUtils.EMPTY;
        }
    }
    /**
     * 获取查询参数中的参数值
     * */
    private static String getSearchObjValue(Object searchObj, String name){
        return  ConvertUtils.toStr(ReflectUtil.getFieldValue(searchObj, name), "");
    }

    /**
     * 特殊字段 class, ids, page, rows, sort, order
     * */
    public static boolean isUsingField(String name){
        return "class".equals(name) || "ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "orders".equals(name);
    }

    /**
     * 获取数据库字段
     * @param field  字段名
     * @param aliasName 表别名 用于多表连接查询
     * */
    public static String getTableColumn(Field field, String aliasName){
        try{
            //如果字段加注解了@TableField(exist = false),不走DB查询
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null && tableField.exist() == false){
                return null;
            }
            if(StringUtils.isNotEmpty(aliasName)){
                return aliasName + "." + StringUtils.toUnderScoreCase(field.getName());
            }else{
                return  StringUtils.toUnderScoreCase(field.getName());
            }
        }catch (Exception e){
            log.error("构造查询条件出错:{}", e.getMessage(),e);
        }
        return null;
    }

    /**
     * 获取数据库字段
     * @param column  字段名
     * @param aliasName 表别名 用于多表连接查询
     * */
    public static String getTableColumn(String column, String aliasName){
        if(StringUtils.isEmpty(aliasName)){
            return StringUtils.toUnderScoreCase(column);
        }else{
            return aliasName + "." + StringUtils.toUnderScoreCase(column);
        }
    }
}
