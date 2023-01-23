package com.tester.testerspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 通过配置文件+注解导入
 * AnnotationDemo上加了@Component注解
 */
public class Application2 {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application_file_context.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

}
