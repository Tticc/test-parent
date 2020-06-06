package com.tester.testerasync.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service
public class AsyncMethodManager {

    /**
     * '@Async("cusThreadPool")':配置好的线程池
     * @return
     */
    @Async("cusThreadPool")
    @SneakyThrows
    public void test_async(){
        Thread.sleep(10*1000);
        System.out.println(Thread.currentThread().getName());
        System.out.println("wake up");
    }
}
