package com.tester.testerwebapp.controller;

import com.tester.testermybatis.dao.domain.OrderItemDomain;
import com.tester.testermybatis.dao.service.OrderItemManager;
import com.tester.testermybatis.prop.ComplexDatabaseShardingAlgorithm;
import com.tester.testermybatis.prop.ComplexTableShardingAlgorithm;
import com.tester.testermybatis.service.MyKeyGenerator;
import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.core.parse.core.rule.registry.ParseRuleRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterWebappApplication.class)
@Slf4j
public class RestTemplateTest implements InitializingBean {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private UserManager userManager;

    private RestTemplate restTemplate;

    @Autowired
    private MyKeyGenerator myKeyGenerator;

    @Autowired
    private OrderItemManager orderItemManager;



    @Test
    public void test_sharding(){
        Long memberId = 9883215L;
        Long aLong = myKeyGenerator.generateOrderNo(memberId, 1);
        OrderItemDomain domain = new OrderItemDomain().init();
        domain.setMemberId(memberId)
                .setOrderNo(aLong)
                .setProductId(0L)
                .setSkuId(0L)
                .setSkuName("name")
                .setSaleQty(new BigDecimal("0.00"))
                .setSalePrice(new BigDecimal("0.00"))
                .setSaleUnit("zhang")
                .setWeight(new BigDecimal("0.00"));
        int insert = orderItemManager.insert(domain);

//        List<OrderItemDomain> orderItemDomains = orderItemManager.listByOrderNo(31131020L);
        System.out.println(domain);
    }

    @Test
    public void test_table(){
        Mono<UserDomain> userDomainMono = userManager.selectUserById(1L);
        UserDomain block = userDomainMono.block();
        System.out.println(block);
    }

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
