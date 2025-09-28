package com.magee.framework.core.exception;

import com.magee.common.exception.BaseException;
import com.magee.common.exception.GlobalException;
import com.magee.common.exception.UtilException;
import com.magee.common.utils.ExceptionUtils;
import com.magee.common.utils.text.StrFormatter;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * 功能描述:全局异常处理器
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * token校验异常（ajax请求返回json，redirect请求跳转页面）
     */
    @ExceptionHandler({AuthenticationException.class})
    public Object handleAuthenticationException(Exception e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.auth(e.getMessage());
    }

    /**
     * 权限校验异常（ajax请求返回json，redirect请求跳转页面）
     */
    @ExceptionHandler({AuthorizationException.class })
    public Object handleAuthorizationException(Exception e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.perm("无权限");
    }

    /**
     * 权限校验异常（ajax请求返回json，redirect请求跳转页面）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handle403(AccessDeniedException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.perm("无权限");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BaseException.class)
    public Object handleBaseException(BaseException e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求地址:{},基础异常:{}", requestURI, e.getMessage());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 全局异常
     */
    @ExceptionHandler(GlobalException.class)
    public Object handleGlobalException(GlobalException e, HttpServletRequest request)   {
        String requestURI = request.getRequestURI();
        log.error("请求地址:{},全局异常:{}", requestURI, e.getMessage(),e);
        return AjaxResult.error(e.getMessage());
    }


    /**
     * 全局异常
     */
    @ExceptionHandler(UtilException.class)
    public Object handleUtilException(UtilException e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求地址:{},工具类异常:{}", requestURI, e.getMessage(),e);
        return AjaxResult.error(e.getMessage());
    }
    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Object handleServiceException(ServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址:{},服务异常:{}", requestURI, e.getMessage());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e ,
                                                             HttpServletRequest request){
        String requestURI = request.getRequestURI();
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("请求:{},参数类型验证:'{}',发生系统异常.", requestURI, message ,e);
        return AjaxResult.error(message);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,})
    public AjaxResult handleMissingParam(MissingServletRequestParameterException e , HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',参数异常 -> param:{}", requestURI, e.getParameterName(),e);
        return AjaxResult.error(StrFormatter.format("参数异常:{}",e.getParameterName()));
    }

    /**
     * @PathVariable 类型转换失败（如 /user/abc 想转成 Long）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',参数类型异常 -> param:{}, message: {}", requestURI, e.getName(),e.getMessage(),e);
        return AjaxResult.error(StrFormatter.format("参数类型异常:{}",e.getName()));
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error(e.getMessage(), requestURI, e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }


    /**
     * 所有 Spring MVC 参数绑定异常
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public AjaxResult handleBind(ServletRequestBindingException e ,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',参数绑定异常 ->  message: {}", requestURI,  e.getMessage(),e);
        return AjaxResult.error("参数异常");
    }
    /**
     * 主键重复
     * */
    @ExceptionHandler(DuplicateKeyException.class)
    public AjaxResult handleDuplicateKeyException(Exception e ,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',主键重复:{}.", requestURI, e.getMessage(),e);
        return AjaxResult.error("主键重复");
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',抛出异常:{},发生未知异常:{}.", requestURI, ExceptionUtils.getRootErrorMessage(e), e.getMessage(),e);
        return AjaxResult.error("发生异常");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)  {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error("发生系统异常");
    }
}
