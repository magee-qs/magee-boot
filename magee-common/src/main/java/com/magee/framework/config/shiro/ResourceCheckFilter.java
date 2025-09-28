package com.magee.framework.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权请求URL访问权限拦截器
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class ResourceCheckFilter  extends AccessControlFilter {

    /**
     * 表示是否允许访问 ，如果允许访问返回true，否则false；
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String url = getPathWithinApplication(servletRequest);
        log.info("当前用户正在访问的 url => " + url);
        return subject.isPermitted(url);
    }

    /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了； 如果返回 true 表示需要继续处理； 如果返回 false
     * 表示该拦截器实例已经处理了，将直接返回即可。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info("当 isAccessAllowed 返回 false 的时候，才会执行 method onAccessDenied ");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.sendRedirect(request.getContextPath() + "/common/401");
        // 返回 false 表示已经处理，例如页面跳转啥的，表示不在走以下的拦截器了
        return false;
    }
}
