package com.tester.testercommon.util.pool;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义线程工厂<br/>
 * org.apache.rocketmq.common.ThreadFactoryImpl<br/>
 * <br/>
 * 使用案例
 * <br/>
 * private final ScheduledExecutorService countProcessorExecutorService = Executors.newSingleThreadScheduledExecutor(new TheThreadFactoryImpl("CountProcessorExecutorService"));
 * @Date 9:36 2021/12/1
 * @Author 温昌营
 **/
public class MyThreadFactoryImpl implements ThreadFactory {
    //

    private final AtomicLong threadIndex = new AtomicLong(0);
    private final String threadNamePrefix;
    private final boolean daemon;

    public MyThreadFactoryImpl(final String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public MyThreadFactoryImpl(final String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadNamePrefix + this.threadIndex.incrementAndGet());
        thread.setDaemon(daemon);
        return thread;
    }
}