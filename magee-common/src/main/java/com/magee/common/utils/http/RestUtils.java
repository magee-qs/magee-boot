package com.magee.common.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * 调用 Restful 接口 Util
 * @author magee
 * @version 1.0
 */
@Slf4j
public class RestUtils {
    private final static RestTemplate  restTemplate;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //ms毫秒
        requestFactory.setConnectTimeout(5000);
        //ms毫秒
        requestFactory.setReadTimeout(15000);
        restTemplate = new RestTemplate(requestFactory);
        // 解决乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
}
