package com.tester.testeraop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TesterAopApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TesterAopApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    public void run(String... args) throws Exception {
        String property = applicationContext.getEnvironment().getProperty("propji.jifo.sjifo");
        System.out.println(property);
    }
}
