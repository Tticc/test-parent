package com.tester.testercommon.pattern.proxy.cglib.cglib;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * 1. 使用net.sf.cglib。开源项目。而非spring的。
 * 2. 保存targetBean，将被代理的原对象保存。（这样，就可以使用 methodProxy.invoke(targetBean,args); 这个方法，
 *  避免调用本实例方法再一次被代理。这也是为什么spring事务注解方法被本类实例方法调用时不起作用）
 * @Date 10:32 2021/8/3
 * @Author 温昌营
 **/
public class DefaultCallbackInterceptor implements MethodInterceptor {

    private final Object targetBean;


    public DefaultCallbackInterceptor(Object targetBean){
        this.targetBean = targetBean;
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("default CGLIB dynamic proxy start");

        Object result = methodProxy.invoke(targetBean,args);
//        System.out.println("default CGLIB dynamic proxy end");
        return result;
    }
}
