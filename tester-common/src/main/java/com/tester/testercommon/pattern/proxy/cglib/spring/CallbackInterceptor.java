package com.tester.testercommon.pattern.proxy.cglib.spring;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 使用spring定义的cglib包内容构建proxy
 * @Date 10:39 2021/8/3
 * @Author 温昌营
 **/
public class CallbackInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("CGLIB dynamic proxy start");
        Object result = methodProxy.invokeSuper(obj,args);
//        System.out.println("CGLIB dynamic proxy end");
        return result;
    }
}
