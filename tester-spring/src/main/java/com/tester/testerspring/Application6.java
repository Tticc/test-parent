package com.tester.testerspring;

import com.tester.testerspring.config.Config6;
import com.tester.testerspring.service.Demo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过registerBean、register导入
 */
public class Application6 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config6.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        // 第二次，将demo注入之后，能打印出demo实例
        context.registerBean("demo1", Demo.class);
        context.register(Demo.class);
        beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
