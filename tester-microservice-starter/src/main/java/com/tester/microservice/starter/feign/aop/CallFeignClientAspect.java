package com.tester.microservice.starter.feign.aop;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import feign.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CallFeignClientAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallFeignClientAspect.class);

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Around("@within(org.springframework.cloud.openfeign.FeignClient)")
    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Class clientInterface = methodSignature.getDeclaringType();
        Object obj = null;

        try {
            obj = pjp.proceed();
            Method targetMethod = methodSignature.getMethod();
            String apiName = clientInterface.getSimpleName() + "." + targetMethod.getName();
            String apiReturnDisposeBeanName = null;
            if (obj == null) {
                apiReturnDisposeBeanName = "Null";
            } else if (obj instanceof RestResult) {
                apiReturnDisposeBeanName = "RestResult";
            } else if (obj instanceof Response) {
                apiReturnDisposeBeanName = "FeignResponse";
            }

            if (apiReturnDisposeBeanName != null) {
                AbstractCallApiReturnDispose apiReturnDispose = AbstractCallApiReturnDispose.getCallApiReturnDispose(apiReturnDisposeBeanName);
                apiReturnDispose.dispose(obj, apiName);
            }

            return obj;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException(5000L, e);
        }
    }
}
