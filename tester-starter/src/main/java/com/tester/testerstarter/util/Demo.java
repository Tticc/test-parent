package com.tester.testerstarter.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletContextAwareProcessor;

/**
 * @Author 温昌营
 * @Date
 */
@Component
public class Demo implements BeanPostProcessor, InitializingBean {
    private String tag = null;
    public String getMyName(){
        return "my name is: com.tester.testerstarter.util.Demo";
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        System.out.println("the tag is:"+this.tag);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("the tag is:"+this.tag);
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.tag = "afterPropertiesSet";
    }
}
