package com.tester.testerstarter.myBeanPostProcessor.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * MethodInterceptor。
 * <br/> 用来设置代理链的行为。
 * <br/><br/> 对于spring的代理，起始点都是：org.springframework.aop.framework.ReflectiveMethodInvocation#proceed()
 * <br/><br/> 而这里的invoke(MethodInvocation invocation)方法的入参就是ReflectiveMethodInvocation的实例。
 * 所以执行到最后都需要执行：invocation.proceed(); 用来递归地执行ReflectiveMethodInvocation#proceed()，直到代理链终点。
 *
 * @Author 温昌营
 * @Date 2021-8-5 16:48:57
 * @see AsyncExecutionInterceptor
 */
@Slf4j
public class MyAnnotationProxyInterceptor implements MethodInterceptor, Ordered {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("i will do nothing!");
        return doProcess(invocation);
    }

    private Object doProcess(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        Class<?>[] parameterTypes = userDeclaredMethod.getParameterTypes();
        Parameter[] parameters = userDeclaredMethod.getParameters();
        Object[] arguments = invocation.getArguments();
        log.info("input：【{}】", getInputValue(parameters, arguments));
        Object reV = invocation.proceed();
        return reV;
    }

    private String getInputValue(Parameter[] parameters, Object[] arguments) {
        if (arguments.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            sb.append(parameters[i].getName()).append(":").append(arguments[i]).append(", ");
        }
        String s = sb.toString();
        return s.substring(0, s.length() - 2);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
