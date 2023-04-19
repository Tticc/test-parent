package com.tester.testerspring.config;


import com.tester.testerspring.config.helper.MyCondition;
import com.tester.testerspring.service.Demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

public class Config21 {
    public Config21(){
        System.out.println("config21 construct : 开始初始化");
    }

    @Bean
    @Conditional({MyCondition.class})
    public Demo demo() {
        return new Demo();
    }

}



