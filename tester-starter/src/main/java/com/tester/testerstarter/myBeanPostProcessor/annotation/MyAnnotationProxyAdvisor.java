package com.tester.testerstarter.myBeanPostProcessor.annotation;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 构建advisor。包括
 * <ol>
 *     <li>pointcut：切点定义。注解：MyAnnotationProxy</li>
 *     <li>advice：操作内容。来自于自定义的MyAnnotationProxyInterceptor</li>
 * </ol>
 * 这些内容都在构造器里被初始化。
 *
 * @Author 温昌营
 * @Date 2021-8-5 16:42:44
 * @see AsyncAnnotationAdvisor
 */
public class MyAnnotationProxyAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public MyAnnotationProxyAdvisor() {
        // 指定唯一注解：MyProxyAnnotation
        Set<Class<? extends Annotation>> myAnnotationTypes = new LinkedHashSet<>(1);
        myAnnotationTypes.add(MyAnnotationProxy.class);

        this.advice = buildAdvice();
        this.pointcut = buildPointcut(myAnnotationTypes);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }


    protected Advice buildAdvice() {
        MyAnnotationProxyInterceptor interceptor = new MyAnnotationProxyInterceptor();
        return interceptor;
    }

    /**
     * Calculate a pointcut for the given async annotation types, if any.
     *
     * @param myAnnotationTypes the async annotation types to introspect
     * @return the applicable Pointcut object, or {@code null} if none
     */
    protected Pointcut buildPointcut(Set<Class<? extends Annotation>> myAnnotationTypes) {
        ComposablePointcut result = null;
        for (Class<? extends Annotation> myAnnotationType : myAnnotationTypes) {
            Pointcut cpc = new AnnotationMatchingPointcut(myAnnotationType, true);
            Pointcut mpc = new AnnotationMatchingPointcut(null, myAnnotationType, true);
            if (result == null) {
                result = new ComposablePointcut(cpc);
            } else {
                result.union(cpc);
            }
            result = result.union(mpc);
        }
        return (result != null ? result : Pointcut.TRUE);
    }
}
