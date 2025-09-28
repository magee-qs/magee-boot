package com.magee.framework.config.shiro;

import com.alibaba.fastjson.JSON;
import com.magee.common.utils.StringUtils;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.common.constant.CommonConstant;
import com.magee.common.utils.ServletUtils;
import com.magee.common.utils.TokenUtils;
import com.magee.common.utils.spring.SpringUtils;
import com.magee.framework.config.mybatis.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 鉴权登录拦截器
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            HandlerMethod method = getHandlerMethod(request);
            if(method!= null && method.hasMethodAnnotation(Anonymous.class)){
                log.info("==============注解Anonymous==============");
                return true;
            }
            executeLogin(request, response);
            return true;
        } catch (Exception ex) {
            return  false;
        }
    }


    /**
     * 失败处理
     * */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)  {
        Subject subject = getSubject(request, response);
        Object principal = subject.getPrincipal();
        if(principal == null){
            // 未登录
            ServletUtils.renderString((HttpServletResponse) response, JSON.toJSONString(AjaxResult.auth("未登录或登录已过期")));
        }else {
            // 权限不足
            ServletUtils.renderString((HttpServletResponse)response, JSON.toJSONString(AjaxResult.perm("无权限")));
        }
        return false;
    }

    /**
     * 登录鉴权
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)  {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = TokenUtils.getToken(httpServletRequest);

        JwtToken jwtToken  = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        // 允许客户端请求方法
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS,PUT,DELETE");
        // 允许客户端提交的Header
        String requestHeaders = httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (StringUtils.isNotEmpty(requestHeaders)) {
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
        }
        // 允许客户端携带凭证信息(是否允许发送Cookie)
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        // 多租户用到
        String tenantId = httpServletRequest.getHeader(CommonConstant.X_TenantId);
        TenantContext.setTenant(tenantId);

        return super.preHandle(request, response);
    }

    /**
     * JwtFilter中ThreadLocal需要及时清除
     *
     * @param request
     * @param response
     * @param exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        log.info("------清空线程中多租户的ID={}------",TenantContext.getTenant());
        TenantContext.clear();
    }


    private HandlerMethod getHandlerMethod(ServletRequest request){
        try{
            RequestMappingHandlerMapping mapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
            HandlerExecutionChain handlerChain = mapping.getHandler((HttpServletRequest) request);
            if (handlerChain != null && handlerChain.getHandler() instanceof HandlerMethod) {
                return (HandlerMethod) handlerChain.getHandler();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
