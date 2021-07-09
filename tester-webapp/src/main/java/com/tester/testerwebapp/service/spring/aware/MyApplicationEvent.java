package com.tester.testerwebapp.service.spring.aware;

import org.springframework.context.ApplicationEvent;

/**
 * @Author 温昌营
 * @Date 2021-7-9 14:53:59
 */
public class MyApplicationEvent extends ApplicationEvent {
    public MyApplicationEvent() {
        this("source");
    }

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MyApplicationEvent(Object source) {
        super(source);
    }
}
