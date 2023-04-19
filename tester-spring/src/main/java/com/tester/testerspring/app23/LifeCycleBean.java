package com.tester.testerspring.app23;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LifeCycleBean {
    private static final Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    public LifeCycleBean(){
        log.info("LifeCycleBean构造函数");
    }

    @Autowired
    public void autowired(@Value("${path}") String path){
        log.info("LifeCycleBean依赖注入path:{}",path);
    }

    @PostConstruct
    public void init(){
        log.info("LifeCycleBean的@PostConstruct");
    }

    @PreDestroy
    public void destroy(){
        log.info("LifeCycleBean的@PreDestroy");
    }

}
