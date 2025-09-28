package com.magee.framework.config.shiro;

import com.magee.common.constant.CacheConstant;
import com.magee.common.exception.BaseException;
import com.magee.common.utils.ObjectUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.TokenUtils;
import com.magee.common.utils.ip.IpUtils;
import com.magee.common.utils.redis.RedisUtils;
import com.magee.common.utils.ServletUtils;
import com.magee.framework.config.ApplicationConfig;
import com.magee.framework.core.vo.UserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录鉴权和获取用户授权
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
     * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
     *
     * @param authenticationToken 用户登录的账号密码信息
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        log.debug("===============Shiro身份认证开始============");
        String token = (String) authenticationToken.getCredentials();
        if (token == null) {
            HttpServletRequest req = ServletUtils.getRequest();
            log.info("————————身份认证失败——————————IP地址:  "+ IpUtils.getIpAddress(req) +"，URL:"+req.getRequestURI());
            throw new AuthenticationException("token为空");
        }
        UserInfo userInfo = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(userInfo, token, getName());
    }

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     *
     * @param principalCollection 身份信息
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("===============Shiro权限校验开始============");
        UserInfo userInfo = null;

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        if(principalCollection != null){
            userInfo = (UserInfo)principalCollection.getPrimaryPrincipal();
            if(userInfo.isAdmin()){
                // 为管理员分配一个代表“所有权限”的自定义Permission对象
                info.addRole("admin");
                info.addStringPermission("*:*:*");
            }else{
                // 添加角色
                if(StringUtils.isNotEmpty(userInfo.getRoles())){
                    info.addRoles(userInfo.getRoles());
                }
                // 添加权限
                if(StringUtils.isNotEmpty(userInfo.getPermissions())){
                    info.addStringPermissions(userInfo.getPermissions());
                }
            }
        }
        return info;
    }

    private UserInfo checkUserTokenIsEffect(String token) throws Exception {
        String userName = JwtUtil.getUserName(token);
        if(userName == null){
            throw new AuthenticationException("token无效");
        }

        boolean verify = JwtUtil.verify(token, userName, ApplicationConfig.getSecret());
        if(verify == false){
            throw new AuthenticationException("token无效");
        }

        log.debug("———校验token是否有效————");
        UserInfo userInfo =  getUserInfo(userName);


        if(userInfo == null){
            throw new AuthenticationException("token已过期");
        }
        return userInfo;
    }

    private UserInfo getUserInfo(String userName){
        UserInfo userInfo = TokenUtils.getUserInfo(userName);
        if(userInfo != null){
           Long expire =  TokenUtils.getExpire(userName);
           // 缓存快到期时刷新缓存(5分钟)
           if(expire < 5 * 60  ){
               log.info("缓存剩余时长:{}不足, 刷新用户缓存时间,key:{}",  expire   ,userName);
                TokenUtils.setExpire(userName);
           }
        }
        return userInfo;
    }

    /**
     * 清楚缓存
     * */
    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
