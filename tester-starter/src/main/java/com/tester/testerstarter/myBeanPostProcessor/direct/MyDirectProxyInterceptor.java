package com.tester.testerstarter.myBeanPostProcessor.direct;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/***
 * MethodInterceptor。
 * <br/> 用来设置代理链的行为。
 * @Date 14:01 2021/8/6
 * @Author 温昌营
 **/
@Slf4j
public class MyDirectProxyInterceptor implements MethodInterceptor, Ordered {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("这是 MyDirectProxyInterceptor. 这里什么也不干!");
        return doProcess(invocation);
    }

    private Object doProcess(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
