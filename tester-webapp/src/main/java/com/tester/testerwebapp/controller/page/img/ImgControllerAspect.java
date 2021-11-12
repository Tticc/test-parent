package com.tester.testerwebapp.controller.page.img;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Date 16:20 2021/11/12
 * @Author 温昌营
 **/
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ImgControllerAspect {


    //匹配ImgController的所有方法，除了getHome和credential
    @Pointcut("execution(* com.tester.testerwebapp.controller.page.img.ImgController.*(..))" +
            "&& !execution(* com.tester.testerwebapp.controller.page.img.ImgController.getHome(..))" +
            "&& !execution(* com.tester.testerwebapp.controller.page.img.ImgController.credential(..))")
    public void aspectMethod() {}//方法签名


    @Around("aspectMethod()")
    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpSession session = request.getSession();
        if (!ImgController.INNER.equals(session.getAttribute(ImgController.USER_TYPE))) {
            session.setAttribute(ImgController.URL_TAG, request.getRequestURI());
            response.sendRedirect(ImgController.HOME_URL);
            return null;
        }
        return pjp.proceed();
    }
}
