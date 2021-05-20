package com.tester.testerwebapp.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class MySpringApplicationRunListener implements SpringApplicationRunListener {

    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        System.out.println("mySpringApplicationRunListener starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("mySpringApplicationRunListener environmentPrepared");
        System.out.println("environmente"+environment);

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("mySpringApplicationRunListener contextPrepared");
        System.out.println("context"+context);

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("mySpringApplicationRunListener contextLoaded");
        System.out.println("context"+context);
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("mySpringApplicationRunListener started");
        System.out.println("context"+context);
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("mySpringApplicationRunListener running");
        System.out.println("context"+context);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("mySpringApplicationRunListener failed");
        System.out.println("context"+context);
        System.out.println("exception"+exception);
    }
}
