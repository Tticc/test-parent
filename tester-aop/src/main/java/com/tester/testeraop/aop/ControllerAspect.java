package com.tester.testeraop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Aspect
@Component
@Order(1)
@Slf4j
public class ControllerAspect {

    @Order(1)
    @Around(value = "execution(* com.tester.testeraop.controller.*.*(..))")
    public Object traceBackgroundThread(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("controller was triggered!");
        Object next;
        Object[] args = pjp.getArgs();
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Class[] definedParamsType = ms.getParameterTypes();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            params.append(arg).append(",");
        }
        log.info("request in. method:{},param:【{}】",ms.getName(),params.toString());
        next = pjp.proceed(args);
        log.info("request completed. method:{},result:{}", ms.getName(), next);
        return next;
    }


    @Pointcut(value = "execution(* com.tester.testeraop.service.*.*(..))")
    public void enhance1(){};


}
