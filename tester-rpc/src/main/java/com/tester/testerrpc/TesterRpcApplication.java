package com.tester.testerrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication//(exclude= {DataSourceAutoConfiguration.class})
public class TesterRpcApplication {
    public static void main(String[] args) {
        SpringApplication.run(TesterRpcApplication.class, args);
    }

}
