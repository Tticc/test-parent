package com.tester.microservice.starter.feign.interceptor;

import feign.Logger.Level;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class MultipartFileSupportConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    @Bean
    @Primary
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(this.messageConverters));
    }

    @Bean
    public Level multipartLoggerLevel() {
        return Level.FULL;
    }
}
