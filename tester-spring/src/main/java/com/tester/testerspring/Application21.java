package com.tester.testerspring;

import com.tester.testerspring.config.Config21;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Map;

/**
 * 测试beanFactory
 */
public class Application21 {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        AbstractBeanDefinition singleton = BeanDefinitionBuilder.genericBeanDefinition(Config21.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config21", singleton);

        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("1-------------------------------------------------------");
        System.out.println("1-------------------------------------------------------");

        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("2-------------------------------------------------------");
        System.out.println("2-------------------------------------------------------");

        // beanFactory的后处理器，用来补充额外的bean定义。例如通过@Bean加入的bean
        Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beansOfType.values().stream().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });
        beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("3-------------------------------------------------------");
        System.out.println("3-------------------------------------------------------");


        // bean的后处理器，针对bean的生命周期的各个阶段提供扩展，例如通过@Autowired，@Resource
        Map<String, BeanPostProcessor> beansOfType1 = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beansOfType1
                .values()
                .stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(beanPostProcessor -> beanFactory.addBeanPostProcessor(beanPostProcessor));
        beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("4-------------------------------------------------------");
        System.out.println("4-------------------------------------------------------");

        // 准备好所有单例。默认地，spring不会提前准备单例，只有调用getBean是才会初始化。
        // 这里通过config21的构造函数打印位置来演示
        beanFactory.preInstantiateSingletons();
        System.out.println("5-------------------------------------------------------");
        System.out.println("5-------------------------------------------------------");
        beanFactory.getBean("config21");
    }
}
