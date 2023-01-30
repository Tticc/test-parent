package com.tester.testerspring;

import com.tester.testerspring.config.Config9;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过实现BeanDefinitionRegistryPostProcessor接口，完成注入。必须配合@Import
 *
 */
public class Application9 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config9.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
