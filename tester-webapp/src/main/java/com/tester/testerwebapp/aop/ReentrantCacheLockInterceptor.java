package com.tester.testerwebapp.aop;

import com.tester.testercommon.annotation.ReentrantCacheLock;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.redis.lock.ReentrantRedisLockHelper;
import com.tester.testercommon.util.redis.lock.ReentrantRedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 可重入锁切面
 * @Author 温昌营
 * @Date
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class ReentrantCacheLockInterceptor {

    @Autowired
    private ReentrantRedisLockHelper reentrantRedisLockHelper;

    @Autowired
    private ReentrantRedisLockUtil reentrantRedisLockUtil;

    @Order(1)
    @Around("@annotation(reentrantCacheLock)")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint, ReentrantCacheLock reentrantCacheLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length >= 1) {
            Object[] args = proceedingJoinPoint.getArgs();

            for(int i = 0; i < parameterNames.length; ++i) {
                ctx.setVariable(parameterNames[i], args[i]);
            }
        }
        String key = reentrantCacheLock.key();
        StringBuffer lockKeyBuffer = new StringBuffer();
        if (key != null && !"".equals(key.trim())) {
            ExpressionParser parser = new SpelExpressionParser();
            lockKeyBuffer.append(parser.parseExpression(reentrantCacheLock.key()).getValue(ctx).toString());
        } else {
            Class clientInterface = methodSignature.getDeclaringType();
            lockKeyBuffer.append(clientInterface.getSimpleName()).append("_").append(methodSignature.getName());
        }
        String lockKey = lockKeyBuffer.toString();
        log.debug("Cache lock key; {}", lockKey);
        int maxRetry = reentrantCacheLock.maxRetry();
        int timeout = reentrantCacheLock.timeout();
        int retryInterval = reentrantCacheLock.retryInterval();
        AtomicReference<Object> obj = new AtomicReference<>();
        reentrantRedisLockHelper.tryLock(lockKey,"",maxRetry,retryInterval,timeout,()->{
            try {
                obj.set(proceedingJoinPoint.proceed());
            } catch (BusinessException be) {
                throw be;
            }catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }
            return null;
        });
        return obj.get();
    }

    @Deprecated
//    @Around("@annotation(reentrantCacheLock)")
    public Object execute_Old(ProceedingJoinPoint proceedingJoinPoint, ReentrantCacheLock reentrantCacheLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length >= 1) {
            Object[] args = proceedingJoinPoint.getArgs();

            for(int i = 0; i < parameterNames.length; ++i) {
                ctx.setVariable(parameterNames[i], args[i]);
            }
        }
        String key = reentrantCacheLock.key();
        StringBuffer lockKeyBuffer = new StringBuffer();
        if (key != null && !"".equals(key.trim())) {
            ExpressionParser parser = new SpelExpressionParser();
            lockKeyBuffer.append(parser.parseExpression(reentrantCacheLock.key()).getValue(ctx).toString());
        } else {
            Class clientInterface = methodSignature.getDeclaringType();
            lockKeyBuffer.append(clientInterface.getSimpleName()).append("_").append(methodSignature.getName());
        }

        String lockKey = lockKeyBuffer.toString();
        log.debug("Cache lock key; {}", lockKey);
        int maxRetry = reentrantCacheLock.maxRetry();
        int timeout = reentrantCacheLock.timeout();
        int retryInterval = reentrantCacheLock.retryInterval();
        int retryCount = 0;
        Object obj = null;
        try {
            while(!this.reentrantRedisLockUtil.getLock(lockKey, timeout)) {
                if (retryCount >= maxRetry) {
                    throw new BusinessException(5008L);
                }

                Thread.sleep((long)retryInterval);
                ++retryCount;
                log.debug("Get lock again; {}, {}", lockKey, retryCount);
            }

            obj = proceedingJoinPoint.proceed();
        } finally {
            this.reentrantRedisLockUtil.removeLock(lockKey);
        }
        return obj;
    }
}
