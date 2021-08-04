package com.tester.testercommon.pattern.proxy.jdk;

import com.tester.testercommon.pattern.proxy.BizManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    private BizManager bizManager;
    ProxyHandler(BizManager bizManager){
        this.bizManager = bizManager;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("JDK dynamic proxy start");
        Object invoke = method.invoke(bizManager, args);
//        System.out.println("JDK dynamic proxy end");
        return invoke;
    }
}
