package com.magee.common.sensitization;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.magee.common.annotation.SensitiveField;
import com.magee.common.utils.SecurityUtils;
import com.magee.common.utils.StringUtils;
import com.magee.framework.core.vo.UserInfo;

import java.io.IOException;

/**
 * 数据脱敏序列化过滤
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class SensitiveJsonSerializer  extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveType sensitizedType;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (sensitization())
        {
            jsonGenerator.writeString(sensitizedType.sensitiveFunc().apply(value));
        }
        else
        {
            jsonGenerator.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        SensitiveField annotation = beanProperty.getAnnotation(SensitiveField.class);
        if(StringUtils.isNotNull(annotation) && ObjectUtil.equal(String.class, beanProperty.getType().getRawClass())){
            this.sensitizedType = annotation.type();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }

    /**
     * 是否需要脱敏处理
     */
    private boolean  sensitization()
    {
        UserInfo userInfo = SecurityUtils.getUserInfo();
        if (userInfo == null) {
            return true;
        }
        // 管理员不脱敏
        return !UserInfo.isAdmin(userInfo.getUserType());
    }
}
