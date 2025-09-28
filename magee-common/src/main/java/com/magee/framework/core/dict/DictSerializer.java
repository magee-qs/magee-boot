package com.magee.framework.core.dict;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.magee.common.annotation.DictField;
import com.magee.common.utils.spring.SpringUtils;
import com.magee.framework.core.service.CommonAPI;

import java.io.IOException;

/**
 * 功能描述: 字典数据序列化
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class DictSerializer extends JsonSerializer<Object>  implements ContextualSerializer {

    private CommonAPI commonAPI;

    /**
     * 字典类型
     * */
    private String dictType;



    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        // 输入字段
        generator.writeObject(value);

        // 如果为 null 直接 return
        if(value == null) return;

        // 根据值读取缓存
        String dictText = commonAPI.getDictText(value.toString(), dictType);
        if(dictText == null )
            return;

        // 注入一个同名的属性
        String fieldName = generator.getOutputContext().getCurrentName();
        generator.writeStringField(fieldName + "_dictText", dictText);
    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        DictField annotation = beanProperty.getAnnotation(DictField.class);
        if(annotation != null ){
            this.dictType = annotation.dictType();
            this.commonAPI = SpringUtils.getBean(CommonAPI.class);
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
