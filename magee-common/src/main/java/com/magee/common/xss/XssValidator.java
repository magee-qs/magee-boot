package com.magee.common.xss;

import com.magee.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述: 自定义xss校验注解实现
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

    private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value))
        {
            return true;
        }
        return !containsHtml(value);
    }


    public static  boolean containsHtml(String value){
        StringBuilder html = new StringBuilder();
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()){
            html.append(matcher.group());
        }
        return pattern.matcher(html).matches();
    }
}
