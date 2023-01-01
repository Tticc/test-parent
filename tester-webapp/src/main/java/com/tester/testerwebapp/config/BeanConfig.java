package com.tester.testerwebapp.config;


import com.tester.feign.client.async.Test1FeignClientFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public Test1FeignClientFallbackFactory test1FeignClientFallbackFactory() {
        return new Test1FeignClientFallbackFactory();
    }

}
