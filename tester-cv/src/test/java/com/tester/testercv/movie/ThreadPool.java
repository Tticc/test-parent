package com.tester.testercv.movie;


import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * movie
 */
public class ThreadPool {

    public static ThreadPoolTaskExecutor threadPoolTaskExecutor;
    static{
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        /** 核心线程数 */
        threadPoolTaskExecutor.setCorePoolSize(6);
        /** 最大线程数 */
        threadPoolTaskExecutor.setMaxPoolSize(64);
        /** 线程队列大小 */
        threadPoolTaskExecutor.setQueueCapacity(4);
        /** 线程最大空闲时间 */
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        /** 线程前缀名 */
        threadPoolTaskExecutor.setThreadNamePrefix("my-async-task-");
        /**
         * 线程拒绝策略 其中我们主要注意的就是拒绝策略方法：setRejectedExecutionHandler(),拒绝策略常用有有这四种
         *
         * ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
         * ThreadPoolExecutor.DiscardPolicy 丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
         * ThreadPoolExecutor.CallerRunsPolicy 由调用线程处理该任务
         */
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
    }
}
