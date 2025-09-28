package com.magee.framework.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.magee.common.config.SystemConfig;
import com.magee.common.constant.CommonConstant;
import com.magee.common.utils.DateUtils;
import com.magee.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

/**
 * 通用配置
 * */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    /**
     * 首页地址
     */
    @Value("${shiro.indexUrl}")
    private String indexUrl;


    /**
     * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("forward:" + indexUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(CommonConstant.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + SystemConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");

    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    /**
     * 全局字符串 -> Date 转换器
     * */
    @Override
    public void addFormatters(FormatterRegistry registry) {
         registry.addConverter(new Converter<String, Date>() {
             @Override
             public Date convert(String source) {
                 if(StringUtils.isEmpty(source)){
                     return null;
                 }else{
                     return DateUtils.parseDate(source);
                 }
             }
         });
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return b -> b.serializerByType(Long.class, ToStringSerializer.instance);
    }
}
