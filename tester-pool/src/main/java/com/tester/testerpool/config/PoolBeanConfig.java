package com.tester.testerpool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置bean
 * @Author 温昌营
 * @Date 2020-8-18 09:52:32
 */
@Slf4j
@Configuration
public class PoolBeanConfig {

    /**
     * schedule线程池。
     * <ol>
     *     <li>核心数：2</li>
     *     <li>初次启动延时：4 min</li>
     *     <li>执行间隔：30 min</li>
     *     <li>任务：未设置</li>
     * </ol>
     * @param
     * @return java.util.concurrent.ScheduledExecutorService
     * @Date 9:57 2020/8/18
     * @Author 温昌营
     **/
    @Bean("scheduledExecutorService")
    public ScheduledExecutorService getScheduledExecutorService() {
        int coreSize = 2;
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(coreSize);
        long initialDelay = 4L;
        long period = 30L;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        pool.scheduleAtFixedRate(() -> {
            //do nothing
        }, initialDelay, period, timeUnit);
        return pool;
    }
}
