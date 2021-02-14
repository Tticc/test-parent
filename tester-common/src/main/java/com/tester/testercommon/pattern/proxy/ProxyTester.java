package com.tester.testercommon.pattern.proxy;

import com.tester.testercommon.pattern.proxy.cglib.BizManagerCGLIB;
import com.tester.testercommon.pattern.proxy.jdk.BizManagerJDK;

public class ProxyTester {
    public static void main(String[] args){
        BizManager proxyIns = BizManagerJDK.getProxy();
        proxyIns.doBiz();

        BizManagerCGLIB proxy = BizManagerCGLIB.getProxy();
        proxy.doBiz();
    }
}
