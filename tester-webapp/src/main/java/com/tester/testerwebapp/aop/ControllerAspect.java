
package com.tester.testerwebapp.aop;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-2147483648)
@Slf4j
public class ControllerAspect {
    @Order(-2147483648)
    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        MDC.put(ConstantList.TRACE_ID_KEY, CommonUtil.getUUID());
        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (BusinessException be) {
            if (be.getExCode() == 5000L) {
                log.error(be.getMessage(), be);
            } else {
                log.warn("{}", be.getMessage());
            }
            obj = this.buildExceptionRestResult(be);
            throw be;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            BusinessException businessException = new BusinessException(5000L, e.getMessage());
            obj = this.buildExceptionRestResult(businessException);
            throw businessException;
        } finally {
        }

        return obj;
    }

    private RestResult buildExceptionRestResult(BusinessException businessException) {
        RestResult restResult = new RestResult();
        restResult.setCode(businessException.getExCode());
        restResult.setMessage(businessException.getMessage());
        return restResult;
    }


//    static {
//        int coreCount = Runtime.getRuntime().availableProcessors();
//        int poolSize = coreCount / 2;
//        if (poolSize <= 0) {
//            poolSize = 1;
//        }
//
//        OUT_PARAMS_THREAD_POOL = new ThreadPoolExecutor(poolSize, coreCount * 2, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(coreCount * 2), new DubheThreadFactory("OutParams"), new CallerRunsPolicy());
//    }
}
