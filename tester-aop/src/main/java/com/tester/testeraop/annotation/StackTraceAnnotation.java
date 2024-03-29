package com.tester.testeraop.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {TYPE})
@Inherited
public @interface StackTraceAnnotation {

    /**
     * 需要记录的操作日志的service全类名。<br>
     * com.xxx.xxx.xxx.office.core.service.support.user.impl.CoContactServiceImpl<br>?或者<br>
     * com.xxx.xxx.xxx.office.core.service.support.user.impl.CoContactService
     * @return
     */
    String[] servicesToBeRecorded() default {};

    String mapper() default "";

}
