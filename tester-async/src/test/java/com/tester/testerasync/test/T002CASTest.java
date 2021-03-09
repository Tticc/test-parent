package com.tester.testerasync.test;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 这里是通过反射的方式强行获取unsafe，
 * 关于unsafe的常规获取方法，看
 * @see AtomicInteger
 */
public class T002CASTest {
    // setup to use Unsafe.compareAndSwapInt for updates
    private static Unsafe unsafe;
    private static long valueOffset;

    static {
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (sun.misc.Unsafe) f.get(null);


            valueOffset = unsafe.objectFieldOffset
                    (T002CASTest.class.getDeclaredField("value"));
            System.out.println("value字段的地址偏移量：" + valueOffset);
        } catch (Exception ex) { throw new Error(ex); }
    }
    private volatile int value = 6;






    @Test
    public void test_myAtomicAdd(){
        System.out.println("xxx");
        int andAddInt = unsafe.getAndAddInt(this, valueOffset, 5);
        System.out.println("ori value is:" + andAddInt);
        System.out.println("new value is:" + value);
    }

    @Test
    public void test_atomic(){
        AtomicInteger integer = new AtomicInteger();
        System.out.println(integer.get());
        int i = integer.incrementAndGet();
    }



    @Test
    public void test_atomic_ref(){
        AtomicReference<Object> atomicReference = new AtomicReference<>();
        System.out.println(atomicReference.get());
    }
    @Test
    public void test_atomic_ref_stamp(){
        AtomicStampedReference<Integer> stamp = new AtomicStampedReference<>(0,1);
        boolean b = stamp.compareAndSet(stamp.getReference(), stamp.getReference() + 4, stamp.getStamp(), stamp.getStamp() + 1);
        System.out.println("new value "+stamp.getReference());
        System.out.println("new stamp "+stamp.getStamp());

    }
}
