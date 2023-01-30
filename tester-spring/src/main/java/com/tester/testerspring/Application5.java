package com.tester.testerspring;

import com.tester.testerspring.config.Config5;
import com.tester.testerspring.service.Demo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过@Import导入
 */
public class Application5 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config5.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
