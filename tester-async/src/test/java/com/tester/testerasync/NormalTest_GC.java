package com.tester.testerasync;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class NormalTest_GC {

    @Test
    public void test_gc() {
        // 打印jvm参数
//        -XX:+PrintCommandLineFlags
        // 打印gc信息
//        -XX:+PrintGCDetails
        // 使用G1收集器
//        -XX:+UseG1GC

        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        map.put(1,"1");

        System.gc();

        System.out.println(Integer.MAX_VALUE);

        WeakReference<Integer> intg = new WeakReference<>(4);
        System.out.println(intg.get());
        intg.clear();
        System.out.println(intg.get());

        PhantomReference<Integer> ri = new PhantomReference<>(3,new ReferenceQueue<>());

    }

}
