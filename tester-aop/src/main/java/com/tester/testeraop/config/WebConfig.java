package com.tester.testeraop.config;

import com.tester.testeraop.intercepts.interceptor.RequireLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @Date 11:35 2020/7/20
 * @Author 温昌营
 **/
@Component
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequireLoginInterceptor requireLoginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requireLoginInterceptor);
    }
}
