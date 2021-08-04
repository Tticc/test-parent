package com.tester.testercommon.pattern.proxy.cglib.spring;

import com.tester.testercommon.pattern.proxy.BizManager;
import org.springframework.cglib.proxy.Enhancer;

public class BizManagerCGLIB implements BizManager {
    public static BizManagerCGLIB getProxy(){
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://tmp");//将动态代理生成的.class文件保存到路径
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BizManagerCGLIB.class);
        enhancer.setCallback(new CallbackInterceptor());
        Object o = enhancer.create();
        return (BizManagerCGLIB)o;
    }

    @Override
    public void doBiz(){
        doCallByBiz();
    }

    public void doCallByBiz(){
        System.out.println("doCallByBiz");
    }
}
