package com.tester.testerstarter.myBeanPostProcessor.annotation;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;


/**
 * BeanPostProcessor
 * 晚于AsyncAnnotationBeanPostProcessor被执行，所以会先执行这里注入的代理方法
 *
 * @Author 温昌营
 * @Date 2021-8-5 16:38:22
 */
public class MyAnnotationProxyBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    private Class<? extends Annotation> asyncAnnotationType;

    /**
     * 设置为调用链第一位。早于@Async
     *
     * @Date 10:07 2021/8/6
     * @Author 温昌营
     **/
    public MyAnnotationProxyBeanPostProcessor() {
        setBeforeExistingAdvisors(true);
    }


    /**
     * 初始化advisor。
     * <br/>仿照：org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor
     *
     * @Date 10:09 2021/8/6
     * @Author 温昌营
     **/
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        MyAnnotationProxyAdvisor advisor = new MyAnnotationProxyAdvisor();
        advisor.setBeanFactory(beanFactory);
        this.advisor = advisor;
    }
}
