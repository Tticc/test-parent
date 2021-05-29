package com.tester.testerasync.test;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 温昌营
 * @Date
 */
public class T007ThreadPoolTest {

    public static void main(String[] args) throws Exception {
        T007ThreadPoolTest t007ThreadPoolTest = new T007ThreadPoolTest();
        t007ThreadPoolTest.test_countDown();
    }

    private static ThreadPoolExecutor threadPoolExecutor;

    /** 定时器线程池*/
    private static ScheduledExecutorService LOG_THREAD_POOL;

    static {
        int queueCapacity = 2000;
        threadPoolExecutor = new ThreadPoolExecutor(8,8,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>(queueCapacity));
        Executors.newScheduledThreadPool(1);


        int coreSize = 2;
        ClassLoader classLoader = ScheduledThreadPoolExecutor.class.getClassLoader();
        LOG_THREAD_POOL = Executors.newScheduledThreadPool(coreSize);
    }


    @Test
    public void test_schedule() throws InterruptedException {

        AtomicInteger integer = new AtomicInteger(0);
        integer.incrementAndGet();
        Runnable run = new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                System.out.println("runing");
                int i = integer.incrementAndGet();
                if(i == 3){
                    System.out.println("error here");
                    throw new Exception();
                }
            }
        };
        long initialDelay = 4L;
        long period = 3L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        LOG_THREAD_POOL.scheduleAtFixedRate(run, initialDelay, period, timeUnit);
        Thread.sleep(30000);
        System.out.println("ji");
    }

    @Test
    public void test_threadPool() throws Exception {
        Runnable run = () -> System.out.println("runing");
        threadPoolExecutor.execute(run);
        Future<Class<Integer>> submit = threadPoolExecutor.submit(run, Integer.class);
        submit.get();
    }

    @Test
    public void test_countDown() throws InterruptedException {
        CountDownLatch c = new CountDownLatch(1);
        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep 5000");
            c.countDown();
        }).start();

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep 2000");
            c.countDown();
        }).start();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep 1000");
            c.countDown();
        });
        thread.start();
        c.await();
        System.out.println("done");
    }


}
