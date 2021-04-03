package com.tester.testerwebapp.service;

import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testermybatis.dao.mapper.OrderMemberMapper;
import com.tester.testermybatis.dao.service.OrderItemManager;
import com.tester.testermybatis.dao.service.OrderMemberManager;
import com.tester.testermybatis.service.MyKeyGenerator;
import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


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

    @Test
    public void test_s(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
    }
}
