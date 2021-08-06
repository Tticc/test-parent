package com.tester.testerstarter.autoconfigure;

import com.tester.testerstarter.myBeanPostProcessor.annotation.MyAnnotationProxyBeanPostProcessor;
import org.springframework.context.annotation.Bean;

/**
 * @Author 温昌营
 * @Date 2021-8-6 09:46:55
 */
public class MyProxyAutoConfiguration {


    /**
     * 实例化自定义的beanPostProcessor。
     * <br/> 可以考虑加个条件注解。
     *
     * @Date 11:21 2021/8/6
     * @Author 温昌营
     **/
    @Bean
    public static MyAnnotationProxyBeanPostProcessor myAwareProxyBeanPostProcessor() {
        return new MyAnnotationProxyBeanPostProcessor();
    }
}
