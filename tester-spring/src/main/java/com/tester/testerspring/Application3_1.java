package com.tester.testerspring;

import com.tester.testerspring.config.Config3_1;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application3_1 {

    // 使用AnnotationConfigApplicationContext，将config的@ComponentScan包下的所有有注解的类扫描为bean
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config3_1.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        // Config31加入@Configuration之后，会变为：com.tester.testerspring.config.Config31$$EnhancerBySpringCGLIB
        // 被CGLIB代理了。
        // 此时调用里面的@Bean方法将会直接从context中获取，没有则创建。
        // 如果没有这个@Configuration，每次都会创建
        // 这个可能有点重要
        Config3_1 config3_1 = context.getBean("config3_1", Config3_1.class);
        System.out.println(config3_1);
        System.out.println(config3_1.otherBean());
        System.out.println(config3_1.otherBean());
        System.out.println(config3_1.otherBean());

    }

}
