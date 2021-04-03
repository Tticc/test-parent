package com.tester.testermytomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 温昌营
 * @Date 2021-4-2 15:15:48
 */

@SpringBootApplication//(exclude= {DataSourceAutoConfiguration.class})
public class TesterMyTomcatApplication {
    public static void main(String[] args) {
        SpringApplication.run(TesterMyTomcatApplication.class, args);
    }
}
