package com.tester.testeraop.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface DataRangeChecker {

    String key() default "";

}
