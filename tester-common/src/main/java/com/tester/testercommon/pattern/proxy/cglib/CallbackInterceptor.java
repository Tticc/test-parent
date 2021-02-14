package com.tester.testercommon.pattern.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CallbackInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("CGLIB dynamic proxy start");
        Object result = methodProxy.invokeSuper(obj,args);
        System.out.println("CGLIB dynamic proxy end");
        return result;
    }
}
