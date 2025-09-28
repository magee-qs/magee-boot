package com.magee.framework.config;

import lombok.Data;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.TimeZone;

/**
 * 程序注解配置
 * */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.magee.**.mapper")
@ConfigurationProperties(prefix = "sys")
@Data
public class ApplicationConfig {
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    /**
     * 密钥
     * */
    private String secret;

    /**
     * 重试次数
     * */
    private  Integer retryCount;

    /**
     * 数据缓存时长（分钟）
     * */
    private Integer cacheExpire;

    /**
     * 默认密码
     * */
    private String defaultPwd;

    private static ApplicationConfig self;

    @PostConstruct
    public void init(){
        self = this;
    }

    public static String getSecret(){ return self.secret;}

    public static String defaultPwd() { return  self.defaultPwd;}

    public static Integer getRetryCount(){ return self.retryCount;}

    public static Integer getCacheExpire(){ return self.cacheExpire;}

}
