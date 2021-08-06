package com.tester.testerstarter.myBeanPostProcessor.direct;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;


/**
 * BeanPostProcessor
 * 这里没有实现 Ordered,也没有实现 PriorityOrdered。所以会是最后一批被注入的BeanPostProcessor
 * <br/> 因此，也会最后被执行。
 *
 * @Author 温昌营
 * @Date 2021-8-5 16:38:22
 * @see AbstractAdvisingBeanPostProcessor
 */
public class MyDirectProxyBeanPostProcessor implements BeanPostProcessor {

    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (!MyDirectInterface.class.isAssignableFrom(targetClass)) {
            // 如果没有继承MyDirectInterface接口，不做代理
            return bean;
        }
        if (bean instanceof Advised) {
            Advised advised = (Advised) bean;
            if (!advised.isFrozen()) {
                // 放到最前面
                advised.addAdvisor(0, this.getAdvisor());
                return bean;
            }
        }
        if (!AopUtils.isAopProxy(bean)) {
            ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
            proxyFactory.addAdvisor(this.getAdvisor());
            return proxyFactory.getProxy(getProxyClassLoader());
        }
        return bean;
    }

    protected Advisor getAdvisor() {
        // 使用DefaultPointcutAdvisor，不做任何切点过滤
        return new DefaultPointcutAdvisor(getAdvice());
    }

    protected Advice getAdvice() {
        return new MyDirectProxyInterceptor();
    }

    /**
     * 准备spring的proxyfactory
     *
     * @Date 14:46 2021/8/6
     * @Author 温昌营
     **/
    protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.setTarget(bean);
        return proxyFactory;
    }

    protected ClassLoader getProxyClassLoader() {
        return this.proxyClassLoader;
    }
}
