package com.tester.testerwebapp.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service("redisManager")
public class RedisManager {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setKey(){
        User user = new User().setName("wenc").setAge(18);
        redisTemplate.opsForValue().set("user:wenc", user);
    }

    public void getKey(){
        Object o = redisTemplate.opsForValue().get("user:wenc");
        User user = (User)o;
        System.out.println(user);
    }
}
