package com.magee.framework.aspect;

import com.magee.common.annotation.PermissionData;
import com.magee.common.utils.*;
import com.magee.framework.config.mybatis.PermissionDataContext;
import com.magee.framework.config.mybatis.PermissionDataContextHolder;
import com.magee.framework.config.mybatis.PermissionDataRule;
import com.magee.framework.core.service.CommonAPI;
import com.magee.framework.core.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 功能描述: 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 * @author magee
 * @version 版本号:1.0.0
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {

    @Autowired
    private CommonAPI commonAPI;


    /**
     * 切面方法
     * */
    @Around("@annotation(permissionData)")
    public Object dataScopeFilter(ProceedingJoinPoint point, PermissionData permissionData) throws Throwable {
        log.info("=====================拦截方法进行数据权限过滤================================");
        HttpServletRequest request =  ServletUtils.getRequest();

        if(permissionData == null){
            return  point.proceed();
        }

        String component = permissionData.component();
        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        requestPath = filterUrl(requestPath);
        log.debug("拦截请求 >> "+requestPath+";请求类型 >> "+requestMethod);

        // 缓存参数
        PermissionDataContext context = new PermissionDataContext();

        // 用户id
        context.setUserId(SecurityUtils.getUserId());

        // 查询数据权限
        String _dataScope = commonAPI.getMenuDataScope(component);
        PermissionDataRule rule = EnumUtils.fromStringQuietly(PermissionDataRule.class, _dataScope);
        // 默认查询所有数据
        if(rule == null){
            context.setRule(PermissionDataRule.User);
        }else {
            context.setRule(rule);
        }


        // 自定义数据权限
        if(rule != null && rule == PermissionDataRule.Custom){
            context.setCustomSql(permissionData.sql());
        }

        // 部门数据
        if(rule != null && rule == PermissionDataRule.Depart){
            context.setDepartId(SecurityUtils.getDepartId());
            //context.setDepartId(100L);
        }

        // 所有部门及下属数据
        if(rule != null && rule == PermissionDataRule.Depart){
            context.setDepartId(SecurityUtils.getDepartId());
            //context.setDepartIds(Arrays.asList(100L, 200L,300L));
        }

        PermissionDataContextHolder.setContext(context);
        log.info("=====================拦截方法进行数据权限过滤================================");

        try{
            return point.proceed();
        }finally {
            PermissionDataContextHolder.clear();
        }

    }

    private String filterUrl(String requestPath){
        String url = "";
        if(ObjectUtils.isNotEmpty(requestPath)){
            url = requestPath.replace("\\", "/");
            url = url.replace("//", "/");
            if(url.indexOf("//")>=0){
                url = filterUrl(url);
            }
        }
        return url;
    }
}
