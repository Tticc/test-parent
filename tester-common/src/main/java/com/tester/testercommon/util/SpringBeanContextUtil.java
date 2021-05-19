package com.tester.testercommon.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * 使用 spi规范将此类注入容器。
 * <ol>
 *     <li>这样可以避免无法其他包的Application启动类无法扫描并实例化此类。</li>
 *     <li>同时可以去掉 @Lazy(false) 注解</li>
 * </ol>
 * <br/>
 * note: resource/META-INF/spring.factories
 * @Author 温昌营
 * @Date 2020-8-21 11:14:58
 */
@Component
@Order(Integer.MIN_VALUE)
@Slf4j
//@Lazy(false)
public class SpringBeanContextUtil implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
    private static ApplicationContext applicationContext;
    private static BeanDefinitionRegistry registry;
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        SpringBeanContextUtil.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Exception var2) {
            return null;
        }
    }
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
        } catch (Exception var3) {
            return null;
        }
    }

    public static ApplicationContext getApplicationContext(){
        Assert.notNull(applicationContext,"applicationContext null");
        return applicationContext;
    }

    public static void registryBean(String name, final Object object) {
        registry.registerBeanDefinition(name, BeanDefinitionBuilder.genericBeanDefinition(object.getClass(), new Supplier() {
            public Object get() {
                return object;
            }
        }).getBeanDefinition());
    }

    public static void registerSingleton(String name, final Object object) {
        ((SingletonBeanRegistry) registry).registerSingleton(name, object);
    }
}
