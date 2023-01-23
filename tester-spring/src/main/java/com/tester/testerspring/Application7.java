package com.tester.testerspring;

import com.tester.testerspring.config.Config7;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 通过实现ImportSelector接口导入。必须配合@Import
 * 在MyImportSelector中可以通过参数annotationMetadata类获取Config7的类属性，进而可以通过特定条件初始化bean
 */
public class Application7 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config7.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
