package com.tester.testerspring;

import com.tester.testerspring.config.Config10;
import com.tester.testerspring.config.Config22;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * applicationContext的4种生成方式
 */
public class Application22 {
    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();

    }


    private static void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("application_file.xml");
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
    private static void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext context =
                new FileSystemXmlApplicationContext("C:\\Users\\18883\\IdeaProjects\\test-parent\\tester-spring\\src\\main\\resources\\application_file.xml");
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
    private static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config10.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
    private static void testAnnotationConfigServletWebServerApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(Config22.class);
    }
}
