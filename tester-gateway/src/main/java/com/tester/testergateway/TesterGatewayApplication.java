package com.tester.testergateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TesterGatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TesterGatewayApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run(String... args) throws Exception {
        String property = applicationContext.getEnvironment().getProperty("tester.gateway.whitelist.enable");
        System.out.println(property);
    }
}
