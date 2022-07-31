package com.tester.testerswing.boot;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 与 MyThreadFactoryImpl 一模一样
 *
 * @Date 2022-7-31 15:23:20
 * @Author 温昌营
 **/
public class SwingThreadFactoryImpl implements ThreadFactory {

    private final AtomicLong threadIndex = new AtomicLong(0);
    private final String threadNamePrefix;
    private final boolean daemon;

    public SwingThreadFactoryImpl(final String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public SwingThreadFactoryImpl(final String threadNamePrefix, boolean daemon) {
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