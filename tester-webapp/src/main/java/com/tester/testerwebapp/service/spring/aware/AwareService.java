package com.tester.testerwebapp.service.spring.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * spring官方文档给出的各种aware
 * @Author 温昌营
 * @Date 2021-7-9 14:53:59
 */
@Service
public class AwareService implements ApplicationEventPublisherAware, BeanClassLoaderAware,
        ApplicationContextAware, BeanFactoryAware, BeanNameAware, LoadTimeWeaverAware,MessageSourceAware,
        NotificationPublisherAware,ResourceLoaderAware, ServletConfigAware, ServletContextAware
{
    private ApplicationEventPublisher applicationEventPublisher;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private String myName;
    private MessageSource messageSource;
    private LoadTimeWeaver loadTimeWeaver;
    private NotificationPublisher notificationPublisher;
    private ResourceLoader resourceLoader;
    private ServletConfig servletConfig;
    private ServletContext servletContext;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void setBeanName(String name) {
        this.myName = name;
    }
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @Override
    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
        this.loadTimeWeaver = loadTimeWeaver;
    }
    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
