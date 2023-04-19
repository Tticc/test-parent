package com.tester.testeraop.intercepts.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;

/**
 * aop流程演示
 * 2023-2-12 13:58:51
 */
public class AdvisorDemo2 {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GenericApplicationContext ctx = new GenericApplicationContext();
        // 解析spring注解
        ctx.registerBean(ConfigurationClassPostProcessor.class);
        ctx.registerBean(Config.class);
        ctx.refresh();
        ctx.close();


    }

    static class Config {
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
            return new AutowiredAnnotationBeanPostProcessor();
        }

        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }

        @Bean
        public MethodInterceptor advice() {
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("notice");
                    Object proceed = invocation.proceed();
                    return proceed;
                }
            };
        }

        /**
         * 1. 如果没有循环依赖，proxy会在init方法后生成
         * 2. 如果有循环依赖，proxy会在构造函数之后生成，然后作为被依赖对象传入其他实例
         * 3. 如果有循环依赖，但是没有proxy，被依赖对象会在执行构造函数后作为空对象传入其他实例
         */
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

    }

    static class Bean1 {
        public Bean1() {
            System.out.println("Bean1()");
        }

        @Autowired
        public void setBean2(Bean2 bean2) {
            System.out.println("setBean2. class: " + bean2.getClass());
        }

        @PostConstruct
        public void init() {
            System.out.println("Bean1.init()");
        }
    }

    static class Bean2 {
        public Bean2() {
            System.out.println("Bean2()");
        }

        public void foo() {
        }

        @Autowired
        public void setBean1(Bean1 bean1) {
            System.out.println("setBean1. class: " + bean1.getClass());
        }

        @PostConstruct
        public void init() {
            System.out.println("Bean2.init()");
        }

    }


}
