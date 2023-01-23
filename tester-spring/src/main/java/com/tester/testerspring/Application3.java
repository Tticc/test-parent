package com.tester.testerspring;

import com.tester.testerspring.config.Config3;
import com.tester.testerspring.service.factory.OtherBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * 通过@ComponentScan导入
 */
public class Application3 {

    // 使用AnnotationConfigApplicationContext，将config的@ComponentScan包下的所有有注解的类扫描为bean
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config3.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        Object demoFactoryBean = context.getBean("demoFactoryBean", OtherBean.class);
//        Object demoFactoryBean = context.getBean("demoFactoryBean", DemoFactoryBean.class); // will be error
        System.out.println("通过DemoFactoryBean创建出来的bean，不是DemoFactoryBean类型。而是：" + demoFactoryBean);
    }

}
