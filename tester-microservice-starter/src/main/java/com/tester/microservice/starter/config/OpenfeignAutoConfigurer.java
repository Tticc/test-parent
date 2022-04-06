package com.tester.microservice.starter.config;

import com.tester.microservice.starter.feign.aop.*;
import com.tester.microservice.starter.feign.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class OpenfeignAutoConfigurer {

    /**
     * FeignResponse
     *
     * @Date 2022-4-5 17:15:14
     * @Author 温昌营
     **/
    @Bean
    public CallFeignClientAspect callFeignClientAspect() {
        return new CallFeignClientAspect();
    }

    /**
     * 请求拦截
     *
     * @Date 2022-4-5 17:15:14
     * @Author 温昌营
     **/
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    /**
     * FeignResponse
     *
     * @Date 2022-4-5 17:15:14
     * @Author 温昌营
     **/
    @Bean("CallApiReturnDispose.FeignResponse")
    public AbstractCallApiReturnDispose feignResponseDispose() {
        return new CallApiReturnFeignResponseDispose();
    }

    /**
     * null
     *
     * @Date 2022-4-5 17:15:14
     * @Author 温昌营
     **/
    @Bean("CallApiReturnDispose.Null")
    public AbstractCallApiReturnDispose nullResponseDispose() {
        return new CallApiReturnNullDispose();
    }

    /**
     * RestResult
     *
     * @Date 2022-4-5 17:15:14
     * @Author 温昌营
     **/
    @Bean("CallApiReturnDispose.RestResult")
    public AbstractCallApiReturnDispose restResultResponseDispose() {
        return new CallApiReturnRestResultDispose();
    }

}
