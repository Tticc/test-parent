package com.tester.testeraop.aop;

import com.tester.testercommon.annotation.CacheLock;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.CommonUtil;
import com.tester.testercommon.util.redis.RedisCacheLockManager;
import com.tester.testercommon.util.redis.ReentrantRedisLockManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @Author 温昌营
* @Date 2020-11-11 18:15:48
 * @see com.tester.testerwebapp.aop.CacheLockInterceptor
 * @see com.tester.testerwebapp.aop.ReentrantCacheLockInterceptor
 */
//@Aspect
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Slf4j
@Deprecated
public class CacheLockInterceptor {
}
