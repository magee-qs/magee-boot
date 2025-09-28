package com.magee.generator.config;

import com.magee.common.config.SystemConfig;
import com.magee.framework.config.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 功能描述:读取代码生成相关配置
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value =  {"classpath:generator.yml"} , factory = YamlPropertySourceFactory.class)
@Data
public class GenConfig {
    /**
     * 作者
     * */
    private String author;
    /**
     * 包名
     * */
    private String packageName;

    private String url;

    private String userName;

    private String password;

    private String savePath;

    private String templatePath;

    private static GenConfig self;


    @PostConstruct
    public void init(){
        self = this;
    }


    public static String getAuthor(){
        return self.author;
    }

    public static String getPackageName(){
        return self.packageName;
    }

    public static String getUrl(){ return self.url;}

    public static String getUserName(){ return self.userName ;}

    public static String getPassword(){ return self.password;}

    public static String getSavePath(){ return self.savePath;}

    public static String getTemplatePath(){return self.templatePath;}
}
