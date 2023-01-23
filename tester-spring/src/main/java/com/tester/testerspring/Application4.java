package com.tester.testerspring;

import com.tester.testerspring.config.Config4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过@ImportResource导入
 */
public class Application4 {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config4.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

    }

}
