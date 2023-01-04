package com.tester.testerwebapp;

import org.junit.Test;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NormalTest_WebApp_ThreadPool {

    private static Executor executor;

    {
        executor = getAsyncPoolsExecutor();
    }

    @Test
    public void test_ascii() throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        List<String> watchPrint = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            final int tmpI = i * 10;
            Thread thread = new Thread(() -> clientThreadMethod(watchPrint, tmpI));
            thread.start();
            list.add(thread);
        }
        for (Thread thread : list) {
            thread.join();
        }
        System.out.println("------------------------------------------------------");
        System.out.println("------------------------------------------------------");
        System.out.println("------------------------------------------------------");
        for (String s : watchPrint) {
            System.out.println(s);
            System.out.println();
        }
    }

    private void clientThreadMethod(List<String> watchPrint, int startIndex) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("stopWatch for:" + Thread.currentThread().getName());
        List<Integer> list = new ArrayList();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        List<CompletableFuture<String>> futures = new ArrayList<>(list.size());
        List<String> resultList = new ArrayList<>(list.size());
        list.stream().forEach(e -> {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                String res = "threadName:" + Thread.currentThread().getName() + ", ele:" + (startIndex + e);
//                System.out.println("result:"+res);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return res;
            }, executor);
            futures.add(completableFuture);
        });
        for (CompletableFuture<String> future : futures) {
            try {
                String return_val = future.get(2, TimeUnit.SECONDS);
                String str = ("current thread: " + Thread.currentThread().getName() + ", return_val = " + return_val);
                System.out.println("result:" + str);
                resultList.add(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                String str = ("TimeoutException, current thread: " + Thread.currentThread().getName());
                System.out.println("error:" + str);
//                e.printStackTrace();
            }
        }
        stopWatch.stop();
        String s = stopWatch.prettyPrint();
        s = ("prettyPrint = " + s + ", resultSize: " + resultList.size());
        watchPrint.add(s);
    }


    public static Executor getAsyncPoolsExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        /** 核心线程数 */
        threadPoolTaskExecutor.setCorePoolSize(6);
        /** 最大线程数 */
        threadPoolTaskExecutor.setMaxPoolSize(6);
        /** 线程队列大小 */
        threadPoolTaskExecutor.setQueueCapacity(20);
        /** 线程最大空闲时间 */
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        /** 线程前缀名 */
        threadPoolTaskExecutor.setThreadNamePrefix("async-test-cv-task-");
        /** 拷贝父线程中的上下文参数 */
        threadPoolTaskExecutor.setTaskDecorator(new DubheDecorator());
        /**
         * 线程拒绝策略 其中我们主要注意的就是拒绝策略方法：setRejectedExecutionHandler(),拒绝策略常用有有这四种
         *
         * ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
         * ThreadPoolExecutor.DiscardPolic 丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
         * ThreadPoolExecutor.CallerRunsPolic 由调用线程处理该任务
         */
        threadPoolTaskExecutor.setRejectedExecutionHandler(new MyCallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    public static class MyCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            System.out.println("come into myReject");
            super.rejectedExecution(r, e);
        }
    }

    public static class DubheDecorator implements TaskDecorator {
        public DubheDecorator() {
        }

        public Runnable decorate(Runnable runnable) {
//            try {
//                Map<String, String> previous = MDC.getCopyOfContextMap();
//                return () -> {
//                    try {
//                        MDC.setContextMap(previous);
//                        runnable.run();
//                    } finally {
//                        MDC.clear();
//                    }
//
//                };
//            } catch (IllegalStateException var3) {
            return runnable;
//            }
        }
    }

}
