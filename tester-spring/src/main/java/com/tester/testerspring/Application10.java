package com.tester.testerspring;

import com.tester.testerspring.config.Config10;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Conditional，测试bean加载控制
 */
public class Application10 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config10.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
