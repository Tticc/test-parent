package com.tester.testerspring;

import com.tester.testerspring.config.Config8;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过实现ImportBeanDefinitionRegistrar接口，完成注入。必须配合@Import
 * 这种方法支持操作beanDefinition来实现bean定义的操作。
 */
public class Application8 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config8.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
