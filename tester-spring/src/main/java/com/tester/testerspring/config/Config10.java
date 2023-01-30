package com.tester.testerspring.config;


import com.tester.testerspring.config.helper.MyCondition;
import com.tester.testerspring.service.Demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

public class Config10 {

    @Bean
    @Conditional({MyCondition.class})
    public Demo demo() {
        return new Demo();
    }

}



