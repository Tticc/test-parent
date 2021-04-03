package com.tester.testerwebapp.service;

import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.service.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterWebappApplication.class)
@Slf4j
public class RedisManagerTest {

    @Autowired
    private RedisManager redisManager;

    @Test
    public void test_redis(){
        redisManager.setKey();
    }
    @Test
    public void test_getredis(){
        redisManager.getKey();
    }
}
