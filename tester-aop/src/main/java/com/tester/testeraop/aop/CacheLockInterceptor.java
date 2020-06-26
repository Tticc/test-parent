package com.tester.testeraop.aop;

import com.tester.testeraop.annotation.CacheLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @Author 温昌营
 * @Date
 */
@Aspect
@Component
@Order(-2147483598)
@Slf4j
public class CacheLockInterceptor {

//    public static final DefaultRedisScript REMOVE_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return -1 end", Long.class);
//    public static final DefaultRedisScript GET_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end", Long.class);


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
        lockKeyBuffer.append("dubhe-lock:");
        if (key != null && !"".equals(key.trim())) {
            ExpressionParser parser = new SpelExpressionParser();
            lockKeyBuffer.append(methodSignature.getName()).append(":").append(parser.parseExpression(key).getValue(ctx).toString());
        } else {
            Class clientInterface = methodSignature.getDeclaringType();
            lockKeyBuffer.append(clientInterface.getSimpleName()).append("_").append(methodSignature.getName());
        }

        String lockKey = lockKeyBuffer.toString();
        int maxRetry = cacheLock.maxRetry();
        int timeout = cacheLock.timeout();
        int retryInterval = cacheLock.retryInterval();
        int retryCount = 0;
        Object obj = null;

        try {
//            while(!this.cacheLockManager.getLock(lockKey, tokenVersion, timeout)) {
//                if (retryCount >= maxRetry) {
//                    throw new Exception();
//                }
//
//                Thread.sleep((long)retryInterval);
//                ++retryCount;
//            }

            obj = proceedingJoinPoint.proceed();
        } finally {
//            this.cacheLockManager.removeLock(lockKey, tokenVersion);
        }

        return obj;
    }
}
