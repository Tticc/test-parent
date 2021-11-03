package com.tester.testerstarter.autoconfigure;

import com.tester.testerstarter.myBeanPostProcessor.annotation.MyAnnotationProxyBeanPostProcessor;
import com.tester.testerstarter.myBeanPostProcessor.direct.MyDirectProxyBeanPostProcessor;
import org.springframework.context.annotation.Bean;

/**
 * 加载自定义的BeanPostProcessor
 * <br/> spring.factories 中配置
 *
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
    public static MyAnnotationProxyBeanPostProcessor myAnnotationProxyBeanPostProcessor() {
        return new MyAnnotationProxyBeanPostProcessor();
    }


    /**
     * 实例化自定义的beanPostProcessor。
     * <br/> 可以考虑加个条件注解。
     *
     * @Date 11:21 2021/8/6
     * @Author 温昌营
     **/
    @Bean
    public static MyDirectProxyBeanPostProcessor myDirectProxyBeanPostProcessor() {
        return new MyDirectProxyBeanPostProcessor();
    }
}
