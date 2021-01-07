package com.tester.testerwebapp.service;

import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testermybatis.dao.mapper.OrderMemberMapper;
import com.tester.testermybatis.model.response.MemberJoinItemVO;
import com.tester.testermybatis.dao.domain.OrderItemDomain;
import com.tester.testermybatis.dao.domain.OrderMemberDomain;
import com.tester.testermybatis.dao.service.OrderItemManager;
import com.tester.testermybatis.dao.service.OrderMemberManager;
import com.tester.testermybatis.service.MyKeyGenerator;
import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import com.tester.testerwebapp.dao.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
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
public class UserServiceTest{

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

    @Test
    public void test_insert(){
        UserDomain userDomain = new UserDomain().init();
        for (int i = 0; i < 10000000; i++) {
            userDomain.setName("wenc"+i).setCellphone("12498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
            userManager.insert_Test(userDomain);
        }
    }
}
