package com.tester.testerwebapp.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;

public class MyApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("now came in onApplicationEvent.... event is :"+event);
        System.out.println("event" + event);
        System.out.println("event.getTimestamp()"+ event.getTimestamp());
    }

    @EventListener
    public ApplicationEvent processBlockedListEvent(ApplicationEvent event) {
        System.out.println("event" + event);
        return null;
    }
}
