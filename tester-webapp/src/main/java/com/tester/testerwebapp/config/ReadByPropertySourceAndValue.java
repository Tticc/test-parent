package com.tester.testerwebapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = {"classpath:demo/props/demo.properties"})
public class ReadByPropertySourceAndValue {
    @Value("${demo.name}")
    private String name;
    @Value("${demo.sex}")
    private int sex;
    @Value("${demo.type}")
    private String type;
    @Override
    public String toString() {
        return "ReadByPropertySourceAndValue{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", type='" + type + '\'' +
                '}';
    }
}