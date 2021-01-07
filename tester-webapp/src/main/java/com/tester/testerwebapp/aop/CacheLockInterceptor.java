package com.tester.testerwebapp.aop;

import com.tester.testercommon.annotation.CacheLock;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.CommonUtil;
import com.tester.testercommon.util.redis.lock.RedisLockHelper;
import com.tester.testercommon.util.redis.lock.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 不可重入锁切面
 * @Author 温昌营
 * @Date
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class CacheLockInterceptor {

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private RedisLockHelper redisLockHelper;


    @Around("@annotation(cacheLock)")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint, CacheLock cacheLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length >= 1) {
            Object[] args = proceedingJoinPoint.getArgs();

            for(int i = 0; i < parameterNames.length; ++i) {
                ctx.setVariable(parameterNames[i], args[i]);
            }
        }
        String key = cacheLock.key();
        StringBuffer lockKeyBuffer = new StringBuffer();
        if (key != null && !"".equals(key.trim())) {
            ExpressionParser parser = new SpelExpressionParser();
            lockKeyBuffer.append(parser.parseExpression(cacheLock.key()).getValue(ctx).toString());
        } else {
            Class clientInterface = methodSignature.getDeclaringType();
            lockKeyBuffer.append(clientInterface.getSimpleName()).append("_").append(methodSignature.getName());
        }

        String lockKey = lockKeyBuffer.toString();
        log.debug("Cache lock key; {}", lockKey);
        int maxRetry = cacheLock.maxRetry();
        int timeout = cacheLock.timeout();
        int retryInterval = cacheLock.retryInterval();
        AtomicReference<Object> obj = new AtomicReference<>();
        String tokenVersion = CommonUtil.getUUID();
        redisLockHelper.tryLock(lockKey,tokenVersion,maxRetry,retryInterval,timeout,()->{
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
//    @Around("@annotation(cacheLock)")
    public Object execute_Old(ProceedingJoinPoint proceedingJoinPoint, CacheLock cacheLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length >= 1) {
            Object[] args = proceedingJoinPoint.getArgs();

            for(int i = 0; i < parameterNames.length; ++i) {
                ctx.setVariable(parameterNames[i], args[i]);
            }
        }
        String key = cacheLock.key();
        StringBuffer lockKeyBuffer = new StringBuffer();
        if (key != null && !"".equals(key.trim())) {
            ExpressionParser parser = new SpelExpressionParser();
            lockKeyBuffer.append(parser.parseExpression(cacheLock.key()).getValue(ctx).toString());
        } else {
            Class clientInterface = methodSignature.getDeclaringType();
            lockKeyBuffer.append(clientInterface.getSimpleName()).append("_").append(methodSignature.getName());
        }

        String lockKey = lockKeyBuffer.toString();
        log.debug("Cache lock key; {}", lockKey);
        int maxRetry = cacheLock.maxRetry();
        int timeout = cacheLock.timeout();
        int retryInterval = cacheLock.retryInterval();
        int retryCount = 0;
        Object obj = null;
        String tokenVersion = CommonUtil.getUUID();

        try {
            while(!this.redisLockUtil.getLock(lockKey, tokenVersion, timeout)) {
                if (retryCount >= maxRetry) {
                    throw new BusinessException(5008L);
                }

                Thread.sleep((long)retryInterval);
                ++retryCount;
                log.debug("Get lock again; {}, {}", lockKey, retryCount);
            }

            obj = proceedingJoinPoint.proceed();
        } finally {
            this.redisLockUtil.removeLock(lockKey, tokenVersion);
        }

        return obj;
    }
}
