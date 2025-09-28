package com.magee.framework.config;

import com.magee.framework.config.shiro.CustomShiroFilterFactoryBean;
import com.magee.framework.config.shiro.JwtAuthenticationFilter;
import com.magee.framework.config.shiro.ShiroRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.*;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * shiro配置
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Autowired(required = false)
    private RedisProperties redisProperties;

    /**
     * Filter Chain定义说明
     *
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();


        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/sys/login","anon"); // 用户登录
        filterChainDefinitionMap.put("/sys/captcha","anon"); // 验证码
        filterChainDefinitionMap.put("/sys/common/static/**", "anon");//图片预览 &下载文件不限制token
        filterChainDefinitionMap.put("/sys/common/pdf/**", "anon");//pdf预览
        filterChainDefinitionMap.put("/generic/**", "anon");//pdf预览需要文件
        // 页面跳转
        filterChainDefinitionMap.put("/common/401", "anon"); // 401响应
        filterChainDefinitionMap.put("/common/403","anon"); // 403响应

        // 排除静态资源后缀
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.gif", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");

        // 排除字体格式的后缀
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.woff", "anon");
        filterChainDefinitionMap.put("/**/*.woff2", "anon");


        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");




        //websocket排除
        filterChainDefinitionMap.put("/websocket/**", "anon");//系统通知和公告
        filterChainDefinitionMap.put("/newsWebsocket/**", "anon");//CMS模块
        filterChainDefinitionMap.put("/vxeSocket/**", "anon");//JVxeTable无痕刷新示例





        //错误路径排除
        filterChainDefinitionMap.put("/error", "anon");
        // 企业微信证书排除
        filterChainDefinitionMap.put("/WW_verify*", "anon");

        filterChainDefinitionMap.put("/test/**", "anon");

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new JwtAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面返回JSON
        shiroFilterFactoryBean.setUnauthorizedUrl("/common/403");
        // 未登录
        shiroFilterFactoryBean.setLoginUrl("/common/401");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //自定义缓存实现,使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        /**
         * 解决重复代理问题 github#994
         * 添加前缀判断 不匹配 任何Advisor
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * cacheManager 缓存 redis实现
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        log.info("===============(1)创建缓存管理器RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("userId");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public IRedisManager redisManager() {
        log.info("===============(2)创建RedisManager,连接Redis..");
        IRedisManager manager;
        if (Objects.nonNull(redisProperties)
                && Objects.nonNull(redisProperties.getSentinel())
                && !CollectionUtils.isEmpty(redisProperties.getSentinel().getNodes())) {
            RedisSentinelManager sentinelManager = new RedisSentinelManager();
            sentinelManager.setMasterName(redisProperties.getSentinel().getMaster());
            sentinelManager.setHost(String.join(",", redisProperties.getSentinel().getNodes()));
            sentinelManager.setPassword(redisProperties.getPassword());
            sentinelManager.setDatabase(redisProperties.getDatabase());

            return sentinelManager;
        }

        // redis 单机支持，在集群为空，或者集群无机器时候使用
        if (lettuceConnectionFactory.getClusterConfiguration() == null || lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().isEmpty()) {
            RedisManager redisManager = new RedisManager();
            redisManager.setHost(lettuceConnectionFactory.getHostName() + ":" + lettuceConnectionFactory.getPort());
            //(lettuceConnectionFactory.getPort());
            redisManager.setDatabase(lettuceConnectionFactory.getDatabase());
            redisManager.setTimeout(0);
            if (!StringUtils.isEmpty(lettuceConnectionFactory.getPassword())) {
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
            }
            manager = redisManager;
        }else{
            // redis集群支持，优先使用集群配置
            RedisClusterManager redisManager = new RedisClusterManager();
            Set<HostAndPort> portSet = new HashSet<>();
            lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().forEach(node -> portSet.add(new HostAndPort(node.getHost() , node.getPort())));

            if (com.magee.common.utils.StringUtils.isNotEmpty(lettuceConnectionFactory.getPassword())) {
                JedisCluster jedisCluster = new JedisCluster(portSet, 2000, 2000, 5,
                        lettuceConnectionFactory.getPassword(), new GenericObjectPoolConfig());
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
                redisManager.setJedisCluster(jedisCluster);
            } else {
                JedisCluster jedisCluster = new JedisCluster(portSet);
                redisManager.setJedisCluster(jedisCluster);
            }
            manager = redisManager;
        }
        return manager;
    }
}
