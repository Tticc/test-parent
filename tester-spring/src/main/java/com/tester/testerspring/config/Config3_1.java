package com.tester.testerspring.config;

import com.tester.testerspring.service.factory.OtherBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.tester.testerspring.service"})
public class Config3_1 {
    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }
}
