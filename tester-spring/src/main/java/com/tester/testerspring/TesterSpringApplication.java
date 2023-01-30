package com.tester.testerspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TesterSpringApplication {

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application_file.xml");
//        Object demo = context.getBean("demo");
//        System.out.println(demo);
        ApplicationContext context = new ClassPathXmlApplicationContext("application_file_context.xml");
        Object annotationDemo = context.getBean("annotationDemo");
        System.out.println(annotationDemo);
    }

}
