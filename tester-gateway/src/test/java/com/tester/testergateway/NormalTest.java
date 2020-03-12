package com.tester.testergateway;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NormalTest {

    @Test
    public void test_length(){
        int length = "WWCISP_G8PYgRaOVHjXWUWFqchpBqqqUpGj0OyR9z6WTwhnMZGCPHxyviVstiv_2fTG8YOJq8L8zJT2T2OvTebANV-2MQ".length();
        System.out.println(length);
    }
    @Test
    public void test_atomic(){
        String str1 = "after comp";
        String str2 = "after after";
        AtomicReference<String> atomicStr = new AtomicReference<>(null);
        atomicStr.compareAndSet(null, str1);
        Assert.assertEquals(str1, atomicStr.get());
        atomicStr.compareAndSet(null,str2);
        Assert.assertNotEquals(str2, atomicStr.get());
    }

    @Test
    public void test_assert_state(){
        String str1 = "null";
        org.springframework.util.Assert.state(str1 != null, () -> "Missing @ConfigurationProperties on ");
    }
    
    @Test
    public void test_atomic_integer() throws InterruptedException {
        AtomicInteger ai = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ai.addAndGet(1);
                ai.addAndGet(2);
                ai.addAndGet(3);
                ai.addAndGet(4);
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(ai);
    }

}
