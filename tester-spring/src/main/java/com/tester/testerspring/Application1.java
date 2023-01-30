package com.tester.testerspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 通过配置文件导入
 */
public class Application1 {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application_file.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

}
