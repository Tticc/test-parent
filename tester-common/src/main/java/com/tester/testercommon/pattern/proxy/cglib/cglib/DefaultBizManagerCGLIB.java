package com.tester.testercommon.pattern.proxy.cglib.cglib;

import com.tester.testercommon.pattern.proxy.BizManager;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

public class DefaultBizManagerCGLIB implements BizManager {


    public static DefaultBizManagerCGLIB getProxy(DefaultBizManagerCGLIB oriBean){
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://tmp");//将动态代理生成的.class文件保存到路径
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultBizManagerCGLIB.class);
        // 设置callback。可以设置一个，也可以设置多个。
//        enhancer.setCallback(new DefaultCallbackInterceptor(oriBean));
        enhancer.setCallbacks(new Callback[]{new DefaultCallbackInterceptor(oriBean)});
        Object o = enhancer.create();
        return (DefaultBizManagerCGLIB)o;
    }

    @Override
    public void doBiz(){

    }
}
