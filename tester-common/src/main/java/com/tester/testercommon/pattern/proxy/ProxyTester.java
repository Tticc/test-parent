package com.tester.testercommon.pattern.proxy;

import com.tester.testercommon.pattern.proxy.cglib.cglib.DefaultBizManagerCGLIB;
import com.tester.testercommon.pattern.proxy.cglib.spring.BizManagerCGLIB;
import com.tester.testercommon.pattern.proxy.jdk.BizManagerJDK;

public class ProxyTester {
    public static void main(String[] args){
        BizManager proxyIns = BizManagerJDK.getProxy();
        proxyIns.doBiz();

        BizManagerCGLIB proxy = BizManagerCGLIB.getProxy();
        proxy.doBiz();

        DefaultBizManagerCGLIB oriBean = new DefaultBizManagerCGLIB();
        DefaultBizManagerCGLIB defaultProxy = DefaultBizManagerCGLIB.getProxy(oriBean);
        defaultProxy.doBiz();
    }
}
