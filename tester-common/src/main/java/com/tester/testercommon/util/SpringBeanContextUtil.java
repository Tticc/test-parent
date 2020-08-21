package com.tester.testercommon.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @Author 温昌营
 * @Date 2020-8-21 11:14:58
 */
@Component
@Order(-2147483648)
@Slf4j
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
    public static void registryBean(String name, final Object object) {
        registry.registerBeanDefinition(name, BeanDefinitionBuilder.genericBeanDefinition(object.getClass(), new Supplier() {
            public Object get() {
                return object;
            }
        }).getBeanDefinition());
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
}
