package com.tester.testerasync;

import com.tester.testerasync.spring.SubClass;
import io.netty.handler.codec.socks.SocksAddressType;
import org.junit.Test;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NormalTest_Async {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);

    @Test
    public void test_change(){
        char c = (char) (8 + '0');
        System.out.println(c);
        char cc = (char) (18 + '0');
        System.out.println(cc);
    }

    @Test
    public void testsdf(){
        SubClass subClass = new SubClass();
        TreeSet<Object> objects = new TreeSet<>();
        objects.add(1);
        objects.add(2);
        objects.add(2);
        objects.add(2);
        System.out.println(objects);

        HashMap<String, String> map = new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        map.put("1","1");
        map.putIfAbsent("1","1");
        System.out.println(map);


        int a = 99;
        Integer aa = new Integer(99);
        System.out.println(aa == a);
    }

    @Test
    public void test_Thread() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("sleeping");
                Thread.currentThread().sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread.currentThread().sleep(3000L);
        thread.interrupt();
        Thread.currentThread().sleep(3000L);

    }
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
