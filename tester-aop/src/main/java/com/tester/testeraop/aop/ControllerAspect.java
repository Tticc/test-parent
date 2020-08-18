package com.tester.testeraop.aop;

import com.alibaba.fastjson.JSON;
import com.tester.testeraop.controller.CloudOfficeException;
import com.tester.testeraop.controller.StackTraceAnnotation;
import com.tester.testeraop.controller.UserOperationDO;
import com.tester.testercommon.controller.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
