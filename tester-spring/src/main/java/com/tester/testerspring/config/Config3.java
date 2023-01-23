package com.tester.testerspring.config;

import com.tester.testerspring.service.factory.DemoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.tester.testerspring.service"})
public class Config3 {
    @Bean
    public DemoFactoryBean demoFactoryBean(){
        return new DemoFactoryBean();
    }
}
