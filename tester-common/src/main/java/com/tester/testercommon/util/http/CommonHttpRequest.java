package com.tester.testercommon.util.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @Author 温昌营
 * @Date 2020-5-14
 */
public class CommonHttpRequest {
    public static <TInput, TOutput> TOutput jsonPost(String url, TInput object, Class<TOutput> outputClazz, Integer timeOut) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity(object, headers);
        if (timeOut != null) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(1000);
            requestFactory.setReadTimeout(timeOut);
            restTemplate.setRequestFactory(requestFactory);
        }
        TOutput returnedOutput = (TOutput) restTemplate.postForObject(url, request, outputClazz);
        return returnedOutput;
    }
}
