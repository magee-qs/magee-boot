package com.magee.framework.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        // 用 Spring 自带的 YamlPropertiesFactoryBean 解析
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        Properties props = factory.getObject();
        // 名字随便给，保证唯一即可
        return new PropertiesPropertySource(name != null ? name : resource.getResource().getFilename(), props);
    }
}
