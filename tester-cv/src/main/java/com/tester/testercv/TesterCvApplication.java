package com.tester.testercv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(scanBasePackages = {"com.tester.testercv", "com.tester.testercommon"})
public class TesterCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesterCvApplication.class, args);
    }

}
