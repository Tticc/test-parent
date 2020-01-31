package com.tester.testerwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.tester.testerwebapp.**.mapper")
@SpringBootApplication
public class TesterWebappApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TesterWebappApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    public void run(String... args) throws Exception {
        String properties = "spring.datasource.url";
        String xxx = applicationContext.getEnvironment().getProperty(properties);
        System.out.println(properties + ": " + xxx);
    }
}
