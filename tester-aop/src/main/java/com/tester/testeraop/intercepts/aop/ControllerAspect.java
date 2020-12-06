package com.tester.testeraop.intercepts.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@Order(1)
@Slf4j
public class ControllerAspect {

    @Autowired
    private ApplicationContext applicationContext;


    @Around(value = "execution(* com.tester.testeraop.controller.*.*(..))")
//    @Around(value = "@annotation(com.tester.testeraop.controller.StackTraceAnnotation)")
    public Object doSomething(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("login-token");

        System.out.println("controller aspect was triggered!");
        Object[] args = pjp.getArgs();
        // 1.获取注解
        for (Object arg : args) {
            String s = JSON.toJSONString(arg);
            System.out.println(s);
        }
        return pjp.proceed(args);
//        RestResult<String> res = new RestResult<>();
//        res.setCode(403);
//        res.setData("CardPlatformConstants.RESULT_FAIL");
//        res.setTimestamp(new Date().getTime());
//        res.setMessage("验签失败");
//        return res;
    }


}
