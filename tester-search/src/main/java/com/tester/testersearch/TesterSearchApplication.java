package com.tester.testersearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Date 15:45 2022/7/27
 * @Author 温昌营
 **/
@SpringBootApplication
@EnableScheduling  // 开启定时任务
public class TesterSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesterSearchApplication.class, args);
    }

}
