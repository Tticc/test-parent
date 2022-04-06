package com.tester.microservice.starter.feign.aop;

import com.tester.base.dto.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractCallApiReturnDispose implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCallApiReturnDispose.class);

    private static ApplicationContext tempApplicationContext;

    public AbstractCallApiReturnDispose() {
    }

    public static AbstractCallApiReturnDispose getCallApiReturnDispose(String name) {
        String beanName = "CallApiReturnDispose." + name;
        AbstractCallApiReturnDispose apiReturnDispose = (AbstractCallApiReturnDispose) tempApplicationContext.getBean(beanName, AbstractCallApiReturnDispose.class);
        return apiReturnDispose;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        tempApplicationContext = applicationContext;
    }

    public abstract void dispose(Object var1, String var2) throws BusinessException;
}
