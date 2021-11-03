package com.tester.testerwebapp.service.spring.lifecycle;

import org.springframework.context.LifecycleProcessor;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date 2021-7-5 15:54:25
 */
@Service
public class LifecycleService implements LifecycleProcessor{

    @Override
    public void onRefresh() {
        System.out.println("come in to onRefresh");
    }

    @Override
    public void onClose() {
        System.out.println("come in to onClose");

    }

    @Override
    public void start() {
        System.out.println("come in to start");

    }

    @Override
    public void stop() {
        System.out.println("come in to stop");

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
