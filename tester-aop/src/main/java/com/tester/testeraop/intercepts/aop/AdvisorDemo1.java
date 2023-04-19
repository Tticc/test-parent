package com.tester.testeraop.intercepts.aop;

import com.tester.testercommon.util.CommonUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 演示aop实际是怎么实现的<br/>
 * 其中有两个重要的方法： findEligibleAdvisors 和 wrapIfNecessary<br/>
 * 2023-2-12 13:58:51
 */
public class AdvisorDemo1 {

    public AdvisorDemo1() throws NoSuchMethodException {
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GenericApplicationContext ctx = new GenericApplicationContext();
        ctx.registerBean("aspect1",Aspect1.class);
        ctx.registerBean("config1",Config1.class);
        // 解析spring注解
        ctx.registerBean(ConfigurationClassPostProcessor.class);
        ctx.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        ctx.refresh();

        /**
         * 第一个重要的方法是findEligibleAdvisors，用来找到有资格的Advisor。由于是protected方法，所以用反射获取。
         *  a. 有资格的Advisor 一部分是低级的，可以自己编写。如下例中的advisor3
         *  b. 有资格的另个部分是高级的，通过解析@Aspect获得
         */
        AnnotationAwareAspectJAutoProxyCreator creator = ctx.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        Method findEligibleAdvisors = CommonUtil.getTargetMethod(creator.getClass(),"findEligibleAdvisors",Class.class, String.class);
        findEligibleAdvisors.invoke(creator,Target2.class,"target2");

        /**
         * 第二个重要的方式是 wrapIfNecessary
         *  a. 它内部就是调用findEligibleAdvisors。只要返回列表不为空，就会创建代理对象
         */
        Method wrapIfNecessary = CommonUtil.getTargetMethod(creator.getClass(),"wrapIfNecessary",Object.class, String.class,Object.class);
        Object o1 = wrapIfNecessary.invoke(creator, new Target1(), "target1", "target1");
        System.out.println("target1's class: "+o1.getClass());
        Object o2 = wrapIfNecessary.invoke(creator, new Target2(), "target2", "target2");
        System.out.println("target2's class: "+o2.getClass());
        ((Target1) o1).foo();

    }



    /**
     * 高级切面
     */
    @Aspect
    static class Aspect1{
        @Before("execution(* foo())")
        public void before(){
            System.out.println("Aspect1 before");
        }
        @After("execution(* foo())")
        public void after(){
            System.out.println("Aspect1 after");
        }
    }

    /**
     * 低级切面
     */
    @Configuration
    static class Config1{
        @Bean
        public Advisor advisor3(MethodInterceptor advice3){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice3);
        }
        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor(){
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("advice3 before");
                    Object proceed = invocation.proceed();
                    System.out.println("advice3 after");
                    return proceed;
                }
            };
        }
    }

    static class Target1 {
        public void foo() {
            System.out.println("Target1.foo");
        }

    }
    static class Target2 {
        public void bar() {
            System.out.println("Target2.bar");
        }
    }
}
