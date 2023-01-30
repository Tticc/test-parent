package com.tester.testerstarter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "test.demo.pro")
public class DemoProperties {
    /**
     * 测试String
     */
    private String a = "a";

    /**
     * 测试int
     */
    private Integer k = 5;
}
