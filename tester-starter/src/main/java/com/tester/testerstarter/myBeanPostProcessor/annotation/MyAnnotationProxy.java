package com.tester.testerstarter.myBeanPostProcessor.annotation;

import java.lang.annotation.*;

/**
 * 注解
 *
 * @Date 17:05 2021/8/5
 * @Author 温昌营
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotationProxy {
}
