package com.tester.testercommon.pattern.proxy.jdk;


import com.tester.testercommon.pattern.proxy.BizManager;

import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 */
public class BizManagerJDK implements BizManager {

    public static BizManager getProxy(){
        Object o = Proxy.newProxyInstance(BizManagerJDK.class.getClassLoader(), new Class<?>[]{BizManager.class}, new ProxyHandler(new BizManagerJDK()));
        return (BizManager)o;
    }

    @Override
    public void doBiz() {

    }
}
