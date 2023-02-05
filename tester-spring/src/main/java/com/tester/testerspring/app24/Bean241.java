package com.tester.testerspring.app24;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Bean241 {
    private static final Logger log = LoggerFactory.getLogger(Bean241.class);

    private Bean242 bean242;

    @Resource
    public void setBean242(Bean242 bean242){
        log.info("@Resource生效.bean242:{}",bean242);
        this.bean242 = bean242;
    }

    @Autowired
    public void autowired(@Value("${path}") String path){
        log.info("@Autowired @Value生效。 Bean241依赖注入path:{}",path);
    }

}
