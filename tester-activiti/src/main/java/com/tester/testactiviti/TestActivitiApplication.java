package com.tester.testactiviti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.tester.testactiviti.**.mapper")
@SpringBootApplication(scanBasePackages = {"com.tester.*"})
public class TestActivitiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestActivitiApplication.class, args);
    }

}
