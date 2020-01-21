package com.tester.testwebapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@MapperScan("com.tester.testwebapp.**.mapper")
@SpringBootApplication
public class TestWebAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TestWebAppApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    public void run(String... args) throws Exception {
        String xxx = applicationContext.getEnvironment().getProperty("xxx");

    }
}
