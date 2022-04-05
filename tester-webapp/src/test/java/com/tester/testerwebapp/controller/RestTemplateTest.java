package com.tester.testerwebapp.controller;

import com.tester.base.dto.controller.RestResult;
import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testermybatis.dao.mapper.OrderMemberMapper;
import com.tester.testermybatis.model.response.MemberJoinItemVO;
import com.tester.testermybatis.dao.domain.OrderItemDomain;
import com.tester.testermybatis.dao.domain.OrderMemberDomain;
import com.tester.testermybatis.dao.service.OrderItemManager;
import com.tester.testermybatis.dao.service.OrderMemberManager;
import com.tester.testermybatis.service.MyKeyGenerator;
import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.controller.mono.UserController;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import com.tester.testerwebapp.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterWebappApplication.class)
@Slf4j
public class RestTemplateTest implements InitializingBean {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private UserManager userManager;

    @Resource
    private UserMapper userMapper;

    private RestTemplate restTemplate;

    @Autowired
    private MyKeyGenerator myKeyGenerator;

    @Autowired
    private OrderItemManager orderItemManager;

    @Autowired
    private OrderMemberManager orderMemberManager;
    @Resource
    private OrderMemberMapper orderMemberMapper;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RedisUtilValue redisUtilValue;
    @Autowired
    private UserController userController;


    @Test
    public void test_page(){
        Mono<RestResult<Serializable>> pageInfo = userController.listByName();
    }
    @Test
    public void test_redisValue(){
        boolean aa = redisUtilValue.setValue("aa", 100L);
        Serializable aa1 = redisUtilValue.getValue("aa");
        System.out.println(aa);
    }

    @Test
    public void test_view(){
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));
        ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("long",11L);
        Object aLong = valueOperations.get("long");
        System.out.println(aLong);
    }
    @Test
    public void test_listOpe(){
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        Long aLong = listOperations.leftPush("ll", "23");
        System.out.println(aLong);
    }

    @Test
    public void test_getAndPrint(){
//        ListOperations<String, Integer> listOperations = redisTemplate.opsForList();
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        listOperations.leftPush("ll","11");
        for(;;){
            List<String> ll = listOperations.range("ll", 0, 10);
            System.out.println(ll);
            String s =  listOperations.leftPop("ll", 4, TimeUnit.SECONDS);
//            if(null != s) {
                System.out.println("got:" + s);
//            }
        }
    }


    @Test
    public void test_table(){
        Mono<UserDomain> userDomainMono = userManager.selectUserById(1L);
        UserDomain block = userDomainMono.block();
        System.out.println(block);
    }

    @Test
    public void test_user(){
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("name")
                .setCellphone("1298301")
                .setEmployeeId("38293")
                .setWechatid("jfijifoa")
                .setEmail("ji9oejifao")
                .setEnname("jfieo")
                ;
        Mono<Integer> insert = userManager.insert();
        Integer block = insert.block();
        System.out.println(block);
    }

    @Test
    public void test_orderMember(){
        List<MemberJoinItemVO> memberJoinItemVOS2 = orderMemberMapper.testJoinTable2(505326337323974657L);
        System.out.println(memberJoinItemVOS2);
        List<MemberJoinItemVO> memberJoinItemVOS = orderMemberManager.testJoinTable(9883215L);
        System.out.println(memberJoinItemVOS.size());
        System.out.println(memberJoinItemVOS);
    }

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
        int insert = orderItemManager.save(domain);

        OrderMemberDomain orderMemberDomain = new OrderMemberDomain().init();
        orderMemberDomain.setMemberAccount("account129039")
                .setMemberId(memberId)
                .setMemberPhone("8374924232")
                .setOrderNo(aLong);
        int insert1 = orderMemberManager.save(orderMemberDomain);
//        List<OrderItemDomain> orderItemDomains = orderItemManager.listByOrderNo(31131020L);
        System.out.println(domain);
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
