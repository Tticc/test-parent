package com.tester.testactiviti.activiti.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 所有写入到流程定义的xml文件里的变量都需要使用这个注解。否则变量无法使用
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ActivitiParamBean {
    String value() default "";
}
