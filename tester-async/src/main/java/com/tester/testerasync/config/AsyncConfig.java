package com.tester.testerasync.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 在方法上添加@Async("cusThreadPool")使用
 * @Author 温昌营
 * @Date
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {


    @Override
    @Bean("cusThreadPool")
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor tp = new ThreadPoolTaskExecutor();
        tp.setCorePoolSize(5);
        tp.setKeepAliveSeconds(15);
        tp.setMaxPoolSize(60);
        tp.setQueueCapacity(2000);
        tp.setThreadNamePrefix("async-tester-task-");
        /*
         * 拒绝策略
         * ThreadPoolExecutor.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常(默认)。
         * ThreadPoolExecutor.DiscardPolicy 丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy 丢弃队列最前面的任务，然后重新尝试执行任务
         * ThreadPoolExecutor.CallerRunsPolicy 由调用线程处理该任务
         */
        tp.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //使用@Bean后，spring容器会自动初始化，不需要执行initialize()
//        tp.initialize();
        return tp;
    }

    /***
     * void方法抛出的异常处理的类,处理void类型方法异常
     * @return org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
     * @Date 9:32 2020/4/24
     * @Author 温昌营
     **/
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return (Throwable ex, Method method, Object... params)->{
            log.info("Exception message - " + ex.getMessage());
            log.info("Method name - " + method.getName());
            for (Object param : params) {
                log.info("Parameter value - " + param);
            }
        };
    }
}
