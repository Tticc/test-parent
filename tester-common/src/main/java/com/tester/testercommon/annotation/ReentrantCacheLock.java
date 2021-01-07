package com.tester.testercommon.annotation;

import com.tester.testercommon.constant.ConstantList;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReentrantCacheLock {
    String key() default "";

    int timeout() default ConstantList.defaultTimeout;

    int maxRetry() default ConstantList.defaultRetryTime;

    int retryInterval() default ConstantList.defaultInterval;
}
