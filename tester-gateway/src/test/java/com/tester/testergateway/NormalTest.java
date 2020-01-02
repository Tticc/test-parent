package com.tester.testergateway;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class NormalTest {

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

}
