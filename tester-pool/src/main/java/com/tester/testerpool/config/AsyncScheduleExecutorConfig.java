package com.tester.testerpool.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author 温昌营
 * @Date 2020-7-31
 */

@Slf4j
@EnableAsync
@Configuration
public class AsyncScheduleExecutorConfig implements AsyncConfigurer {


    @Bean("myThreadPoolTaskScheduler")
    public ThreadPoolTaskScheduler getMyThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        /** 核心线程数 */
        threadPoolTaskScheduler.setPoolSize(6);
        /** 线程前缀名 */
        threadPoolTaskScheduler.setThreadNamePrefix("my-schedule-async-task-");
        /**
         * 线程拒绝策略 其中我们主要注意的就是拒绝策略方法：setRejectedExecutionHandler(),拒绝策略常用有有这四种
         *
         * ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
         * ThreadPoolExecutor.DiscardPolicy 丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
         * ThreadPoolExecutor.CallerRunsPolicy 由调用线程处理该任务
         */
        threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //使用@Bean后，spring容器会自动初始化，不需要执行initialize()
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;

    }
    /**
     * void方法抛出的异常处理的类,处理void类型方法异常
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("Exception message - " + ex.getMessage());
            log.info("Method name - " + method.getName());
            for (Object param : params) {
                log.info("Parameter value - " + param);
            }
        }

    }
}
