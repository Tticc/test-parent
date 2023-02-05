package com.tester.testerspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Locale;

@SpringBootApplication
public class TesterSpringApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext ctx = SpringApplication.run(TesterSpringApplication.class, args);
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }

        // 国际化
        String kk = ctx.getMessage("kk", null, Locale.CHINA);
        System.out.println(kk);

        // 找资源 1
        Resource resource = ctx.getResource("classpath:application_file.xml");
        System.out.println("normal resource: "+resource);
        // 找资源 2
        Resource[] resource2 = ctx.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource1 : resource2) {
            System.out.println("jar resource: "+resource1);
        }

        // 环境变量
        String path = ctx.getEnvironment().getProperty("path");
        System.out.println("path = " + path);

        // 发送事件
//        ctx.publishEvent(new ApplicationStartedEvent());
    }

}
