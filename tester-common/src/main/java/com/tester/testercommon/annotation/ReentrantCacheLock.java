package com.tester.testercommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReentrantCacheLock {
    String key() default "";

    int timeout() default 10000;

    int maxRetry() default 10;

    int retryInterval() default 100;
}
