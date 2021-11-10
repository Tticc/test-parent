package com.tester.testercommon.util.pool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 懒加载单例线程池
 *
 * @Author 温昌营
 * @Date 2021-11-10 14:35:55
 */
public class DefaultPool {

    private DefaultPool() {
    }

    public static void exec(Runnable runnable) {
        PoolInstance.threadPool.execute(runnable);
    }

//    public static ExecutorService getPool(){
//        return PoolInstance.threadPool;
//    }

    private static class PoolInstance {
        private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}
