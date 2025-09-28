package com.magee.framework.config.mybatis;

import com.magee.common.utils.SecurityUtils;
import com.magee.common.utils.reflect.ReflectUtils;
import com.magee.framework.core.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 * @Author magee
 *
 */
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }

        if (SqlCommandType.INSERT == sqlCommandType) {
            UserInfo userInfo = SecurityUtils.getUserInfo();
            Field[] fields = ReflectUtils.getAllFields(parameter);
            for (Field field: fields){
                log.debug("------field.name------" + field.getName());
                try{
                    if("createBy".equals(field.getName())){
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createBy == null || local_createBy.equals("")) {
                            if (userInfo != null) {
                                // 登录人账号
                                field.setAccessible(true);
                                field.set(parameter, userInfo.getUserId());
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createDate == null || local_createDate.equals("")) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                }catch (Exception ex){
                    log.error("mybatis自动赋值失败:name:{}, value:{},message:{}", field.getName(),field.get(parameter), ex.getMessage());
                }
            }
        }

        if (SqlCommandType.UPDATE == sqlCommandType) {
            UserInfo userInfo = SecurityUtils.getUserInfo();
            Field[] fields = null;
            if(parameter instanceof MapperMethod.ParamMap){
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                if (parameter == null) {
                    return invocation.proceed();
                }
                fields = ReflectUtils.getAllFields(parameter);
            }else{
                fields = ReflectUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        //获取登录用户信息
                        if (userInfo != null) {
                            // 登录账号
                            field.setAccessible(true);
                            field.set(parameter, userInfo.getUserId());
                            field.setAccessible(false);
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    log.error("mybatis自动赋值失败:name:{}, value:{},message:{}", field.getName(),field.get(parameter), e.getMessage());
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }
}
