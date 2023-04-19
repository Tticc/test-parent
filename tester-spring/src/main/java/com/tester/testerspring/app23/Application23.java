package com.tester.testerspring.app23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * bean 生命周期示例
 */
@SpringBootApplication(scanBasePackages = {"com.tester.testerspring.app23"})
public class Application23 {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application23.class);
        // 执行销毁
        ctx.close();
    }

}
