package com.tester.testeraop.intercepts.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * aop流程演示
 * 2023-2-12 13:58:51
 */
public class AdvisorDemo {

    public static void main(String[] args) {

        // 1. 指定切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // execution(* foo())：任意返参 foo方法
        pointcut.setExpression("execution(* foo())");

        // 2. 声明通知
        // org.aopalliance.intercept.MethodInterceptor
        MethodInterceptor advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before aop");
                Object proceed = invocation.proceed();
                System.out.println("after aop");
                return proceed;
            }
        };

        // 3. 准备切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

        // 4. 创建代理 - 使用spring提供的ProxyFactory
        Target1 target1 = new Target1();
        ProxyFactory factory = new ProxyFactory();
        // 设置目标对象、设置切面
        factory.setTarget(target1);
        factory.addAdvisor(advisor);
        // 默认地，ProxyFactory不知道目标类有没有实现接口，所以需要指定，否则认为没有实现接口，采用cglib
        factory.setInterfaces(Target1.class.getInterfaces());
        T1 proxy = (T1) factory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();
        proxy.bar();
    }

    interface T1 {
        void foo();

        void bar();
    }

    static class Target1 implements T1 {
        @Override
        public void foo() {
            System.out.println("Target1.foo");
        }

        @Override
        public void bar() {
            System.out.println("Target1.bar");
        }
    }
}
