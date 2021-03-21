package com.tester.testerasync;

import io.netty.handler.codec.socks.SocksAddressType;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NormalTest_Async {

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    @Test
    public void test_suspend(){
        Thread xxx = new Thread(() -> System.out.println("xxx"));
        xxx.start();
        xxx.stop();
        xxx.suspend();
    }

    @Test
    public void test_classLoader() throws Exception {
        String s = "";
        ClassLoader classLoader = this.getClass().getClassLoader();
        System.out.println(classLoader);
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);
        ClassLoader classLoader2 = SocksAddressType.class.getClassLoader();
        System.out.println(classLoader2);
        ClassLoader classLoader3 = Boolean.class.getClassLoader();
        System.out.println(classLoader3);
        Thread.sleep(11);
    }
    @Test
    public void test_syncLock(){
        testlock();
    }

    public void testlock() {
        synchronized (this) {
            System.out.println("xxx");
        }
    }





    static {
        System.out.println("static block");
    }
}
