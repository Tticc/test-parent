package com.tester.testeraop.aop;

import com.tester.testeraop.annotation.DataRangeChecker;
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
import org.springframework.util.StringUtils;

/**
 * 2020-6-26
 */
@Order
@Aspect
@Component
@Slf4j
public class DataRangeInterceptor {

    @Around("@annotation(dataRangeChecker)")
    public Object checkDataRange(ProceedingJoinPoint pjp, DataRangeChecker dataRangeChecker) throws Throwable {
        Object[] args = pjp.getArgs();
        String key = dataRangeChecker.key();
        if(!StringUtils.hasText(key)){
            return pjp.proceed(args);
        }
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length >= 1) {
            for(int i = 0; i < parameterNames.length; ++i) {
                ctx.setVariable(parameterNames[i], args[i]);
            }
        }
        Object obj;
        try {
            Long currentUser = 89L;
            ExpressionParser parser = new SpelExpressionParser();
            Long orgId = Long.valueOf(parser.parseExpression(key).getValue(ctx).toString());

            obj = pjp.proceed(args);
        }finally {

        }

        return obj;
    }

}
