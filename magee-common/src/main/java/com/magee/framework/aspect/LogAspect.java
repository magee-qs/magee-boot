package com.magee.framework.aspect;

import com.alibaba.fastjson.JSON;
import com.magee.common.annotation.AutoLog;
import com.magee.common.filter.PropertyPreExcludeFilter;
import com.magee.common.utils.*;
import com.magee.common.utils.ip.IpUtils;
import com.magee.framework.core.domain.SysLog;
import com.magee.framework.core.service.CommonAPI;
import com.magee.framework.core.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 功能描述: 操作日志记录处理
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {


    @Autowired
    private CommonAPI commonAPI;


    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

    /** 计算操作消耗时间 */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(autoLog)")
    public void doBefore(JoinPoint joinPoint, AutoLog autoLog)   {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(autoLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, AutoLog autoLog, Object jsonResult)  {
        log.info("主线程={}",Thread.currentThread().getName());
        handleLog(joinPoint, autoLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(autoLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, AutoLog autoLog, Exception e)  {
        // 异步写入日志
        handleLog(joinPoint, autoLog, e, null);
    }

    /**
     * 处理日志,使用独立线程池
     * */
    public void handleLog(final JoinPoint joinPoint, AutoLog autoLog, final Exception e, Object jsonResult){
        try{
            log.info("异步线程开始写入日志");
            // 获取当前的用户
            UserInfo userInfo = SecurityUtils.getUserInfo();

            // *========数据库日志=========*//
            SysLog syslog = new SysLog();
            syslog.setStatus(1);
            // 请求的地址
            String ip = IpUtils.getIpAddress(ServletUtils.getRequest());
            syslog.setOperateIp(ip);
            syslog.setUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            if(userInfo != null){
                syslog.setOperateName(userInfo.getRealName());
                Long departId = userInfo.getDeptId();
                if(ObjectUtils.isNotNull(departId)){
                    String departName = commonAPI.getDepartName(departId);
                    syslog.setOperateDepart(departName);
                }
            }

            if(e != null){
                syslog.setStatus(0);
                syslog.setErrorMsg(StringUtils.substring(ConvertUtils.toStr(e.getMessage(),
                        ExceptionUtils.getExceptionMessage(e)), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            syslog.setController(className + "." + methodName + "()");
            // 设置请求方式
            syslog.setMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, autoLog, syslog, jsonResult);
            // 设置消耗时间
            syslog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 操作时间
            syslog.setOperateTime(new Date());
            // 保存日志
            commonAPI.addLog(syslog);


            log.info("异步线程写入日志完成");
        }catch (Exception ex){
            log.error("异常信息:{}", ex.getMessage());
            ex.printStackTrace();
        }finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, AutoLog log, SysLog operLog, Object jsonResult) throws Exception
    {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperateType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveRequestData() && StringUtils.isNotNull(jsonResult))
        {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysLog operLog, String[] excludeParamNames) throws Exception
    {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getMethod();
        if (StringUtils.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name()))
        {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setRequestParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            operLog.setRequestParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (StringUtils.isNotNull(o) && !isFilterObject(o))
                {
                    try
                    {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames)
    {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }


}
