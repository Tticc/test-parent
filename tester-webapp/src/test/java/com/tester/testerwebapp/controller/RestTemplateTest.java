package com.tester.testerwebapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.tester.testerwebapp.TesterWebappApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterWebappApplication.class)
@Slf4j
public class RestTemplateTest implements InitializingBean {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5L))
                .setReadTimeout(Duration.ofSeconds(10L))
                .build();
    }

    @Test
    public void test_restTemplate(){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://www.baidu.com", String.class);
        if(forEntity.getStatusCodeValue() == 200){
            System.out.println(forEntity.getBody());
        }
    }
    @Test
    public void test_date(){
        LocalDate startDate = LocalDate.of(2015, 2, 20);
        LocalDate endDate = LocalDate.of(2017, 1, 25);
        Duration between = Duration.between(endDate.atStartOfDay(), startDate.atStartOfDay());
        Period period = Period.between(endDate, startDate);
        log.info("Years:" + period.getYears() +
                " months:" + period.getMonths() +
                " days:"+period.getDays());
        log.info("seconds:{}", between.getSeconds());
    }
}
