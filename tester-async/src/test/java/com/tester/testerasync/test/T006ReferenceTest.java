package com.tester.testerasync.test;

import org.junit.Test;

import java.lang.ref.*;
import java.util.WeakHashMap;

public class T006ReferenceTest {

    @Test
    public void test_weekReference() throws Exception {

        // 软引用没有ReferenceQueue
        SoftReference<Integer> softReference = new SoftReference<>(1);

        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        map.put(1,"1");

        // 弱引用和虚引用都有ReferenceQueue,gc后会进入ReferenceQueue
        // !!! 注意，进入ReferenceQueue的仅仅是WeakReference对象，而不是WeakReference对象持有的 referent
        // 如下例子，进入ReferenceQueue的是：intg，而intg的引用referent： integer则已经被回收，
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Integer integer = new Integer(4);
        WeakReference<Integer> intg = new WeakReference<>(integer,queue);
        integer = null;
        // gc 之前，存在
        System.out.println(intg.get());
        System.gc();
        Thread.sleep(500);
        // gc 之后，null
        System.out.println(intg.get());
        Reference<?> poll = queue.poll();
        // gc 之后， ReferenceQueue 里面的值是intg
        System.out.println(poll == intg);
        // gc 之后， ReferenceQueue 里面的值是intg，里面的referent：integer已被回收，变为null
        System.out.println(poll.get());


    }


    @Test
    public void test_phantomReference() throws Exception {

        // 弱引用和虚引用都有ReferenceQueue,gc后会进入ReferenceQueue
        ReferenceQueue<Integer> queue = new ReferenceQueue<>();
        PhantomReference<Integer> ri = new PhantomReference<>(3,new ReferenceQueue<>());
    }

}
