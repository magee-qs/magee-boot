package com.magee.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 功能描述: freeMark模板读取
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class FreeMarkerUtils {
    /**
     * 模板生成
     * */
    public static String process(Map<String,Object> model, ClassLoader classLoader ,String templatePath ,String ftl)  {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding("UTF-8");
        try{
            configuration.setClassLoaderForTemplateLoading(classLoader, templatePath);
            // 读取模板
            Template template   = configuration.getTemplate(ftl);

            // 渲染模板
            StringWriter out = new StringWriter();
            template.process(model, out);
            String result = out.toString();
            return result;
        }catch (IOException ex){
            log.error("设置模板路径出错:{}", ex.getMessage(),ex);
            return null;
        }catch (TemplateException ex){
            log.error("读取模板出错:{}", ex.getMessage(),ex);
            return null;
        }
    }
}
