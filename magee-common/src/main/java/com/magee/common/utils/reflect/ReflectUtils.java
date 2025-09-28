package com.magee.common.utils.reflect;

import cn.hutool.core.util.ReflectUtil;
import com.magee.common.exception.UtilException;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.spring.SpringUtils;
import com.magee.common.utils.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数
 * @author magee
 * @version 1.0
 */
@Slf4j
public class ReflectUtils   {
    /**
     * 字段赋值
     * */
   public static void setProperty(Object bean , String property, Object value){
       Field field = getField(bean.getClass(), property);
       if(field == null){
           throw new UtilException(StrFormatter.format("反射读取字段{}不存在", property));
       }
       try{
           field.setAccessible(true);
           field.set(bean,value);
       } catch (Exception e) {
           log.error("反射写入字段{}出错{}",property,e.getMessage());
       }
   }
    /**
     * 字段取值
     * */
   public static <T> T getProperty(Object bean, String property){
       Field field = getField(bean.getClass(), property);
       if(field == null){
           throw new UtilException(StrFormatter.format("反射读取字段{}不存在", property));
       }
       try{
           field.setAccessible(true);
           return (T) field.get(bean);
       } catch (Exception e) {
           log.error("反射读取字段{}出错{}",property,e.getMessage());
           return null;
       }
   }
    /**
     * 获取字段
     * */
    public static Field getField(Class<?> clazz, String property) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(property);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new IllegalArgumentException(
                "Field '" + property + "' not found in " + clazz);
    }


    /**
     * 获取字段
     * */
    public static Field[] getAllFields(Object entity){

        if(entity != null){
            return ReflectUtil.getFields(entity.getClass());
        }else{
            return new Field[]{};
        }
    }

    /**
     * 获取字段
     * */
    public static Field[] getAllFields(Class<?> clazz){
        if(clazz != null){
            return ReflectUtil.getFields(clazz);
        }else{
            return new Field[]{};
        }
    }

    /**
     * 反射调用方法  classA.doMethod('a',1, 1L)
     * @param className 类名
     * @param methodName 方法名
     * @param argStr 参数
     * */
    public static  Object invokeMethod(String className,String methodName, String argStr)   {

        Class<?> clazz =null;
        try{
            clazz = Class.forName(className);
        }catch (Exception ex){
            log.error("没有找到可运行的类:{}", className);
            return null;
        }
        Object bean = SpringUtils.getBean(clazz);
        Method method = findMethod(clazz, methodName);
        if(method == null){
            log.error("反射没有在{}中找到{}方法",className, methodName);
            return null;
        }

        // 解析参数
        String[] argArray = getArgArray(argStr);
        Class<?>[] paramTypes = new Class[argArray.length];
        Object[] args = new Object[argArray.length];

        for(int i = 0 ; i< argArray.length; i++){
            ParamInfo paramInfo = parseParam(argArray[i].trim());
            paramTypes[i] = paramInfo.getType();
            args[i]  = paramInfo.getValue();
        }
        try{
            // 执行方法
            method.setAccessible(true);
            return   method.invoke(bean,args);
        }catch (Exception ex){
            log.error("反射执行失败,类名:{},方法:{}, 参数:{},异常:{}", className, methodName, argStr, ex.getMessage());
            log.error("异常信息:",ex);
            return null;
        }
    }

    /**
     * 根据方法名找到方法
     * */
    private static Method findMethod(Class<?>clazz, String name){
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) return m;
        }
        return null;
    }



    private static String[] getArgArray(String argStr){
        if(StringUtils.isEmpty(argStr)){
            return new String[0];
        }
        return argStr.split(",");
    }

    private static ParamInfo parseParam(String paramStr) {
        paramStr = paramStr.trim();

        // 字符串类型
        if (paramStr.startsWith("\"") && paramStr.endsWith("\"")) {
            return new ParamInfo(String.class, paramStr.substring(1, paramStr.length() - 1));
        }

        // 整数类型
        if (paramStr.matches("-?\\d+")) {
            return new ParamInfo(int.class, Integer.parseInt(paramStr));
        }

        // 长整型
        if (paramStr.matches("-?\\d+L")) {
            return new ParamInfo(long.class, Long.parseLong(paramStr.substring(0, paramStr.length() - 1)));
        }

        // 浮点数
        if (paramStr.matches("-?\\d+\\.\\d+")) {
            return new ParamInfo(double.class, Double.parseDouble(paramStr));
        }

        // 布尔值
        if ("true".equalsIgnoreCase(paramStr) || "false".equalsIgnoreCase(paramStr)) {
            return new ParamInfo(boolean.class, Boolean.parseBoolean(paramStr));
        }

        // 默认作为字符串处理
        return new ParamInfo(String.class, paramStr);
    }

    private static class ParamInfo {
        private Class<?> type;
        private Object value;

        public ParamInfo(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Class<?> getType() { return type; }
        public Object getValue() { return value; }
    }
}
