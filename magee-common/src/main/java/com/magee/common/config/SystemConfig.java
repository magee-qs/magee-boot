package com.magee.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@ConfigurationProperties(prefix = "magee")
@Data
public class SystemConfig {
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 年度 */
    private String copyrightYear;

    /** 上传路径 */
    private String profile;


    private static SystemConfig self;


    @PostConstruct
    public void init(){
        self = this;
    }

    public static String getName() {
        return self.name;
    }


    public static String getVersion() {
        return self.version;
    }


    public static String getCopyrightYear() {
        return self.copyrightYear;
    }


    public static String getProfile() {   return self.profile;  }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return  getProfile()+ "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }


    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
