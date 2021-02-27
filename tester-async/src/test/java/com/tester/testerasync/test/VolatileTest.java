package com.tester.testerasync.test;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTest {
    // volatile 不保证原子性
    @Test
    public void test_atomic(){
        DataClass1 data = new DataClass1();
        for (int i = 0; i < 20; i++) {
            newThread(() -> {
                for (int j = 0; j < 1000; j++) {
                    data.add();
//                    data.syncAdd();
                }
            });
        }
        while(Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println("aa was changed. "+data.aa);

    }

    // volatile 保证可见性
    @Test
    public void test_viewable() throws InterruptedException {
        DataClass1 data = new DataClass1();
        newThread(() -> {
            try {
                Thread.sleep(2120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1111: "+data.aa);
            data.set();
            System.out.println("1111: "+data.aa);
        });
        newThread(() -> {
            System.out.println("2222: "+data.aa);
//            while (data.aa == 0){
            while (data.aa.get() == 0){
            }
            System.out.println("2222: "+data.aa);
        });
        newThread(() -> {
            System.out.println("3333: "+data.aa);
//            while (data.aa == 0){
            while (data.aa.get() == 0){
            }
            System.out.println("3333: "+data.aa);
        });


        while(Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println("aa was changed. "+data.aa);
    }

    private void newThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.start();
    }


    class DataClass1 {

        //        volatile int aa = 0;
        volatile AtomicInteger aa = new AtomicInteger(0);

        public void set(){
//            aa = 222;
            aa = new AtomicInteger(222);
        }

        public void add(){
//            ++aa;
            aa.incrementAndGet();
        }

        public synchronized void syncAdd(){
//            ++aa;
            aa.incrementAndGet();
        }
    }
}
