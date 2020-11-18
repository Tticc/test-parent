package com.tester.testercommon.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component("redisUtilList")
@Slf4j
public class RedisUtilList {
    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    /**
     * 前缀
     */
    public static final String KEY_PREFIX_VALUE = "tester:redis:::value::";

    public void leftPush(String k, Serializable v){
        String key = KEY_PREFIX_VALUE + k;
        try {
            ListOperations<Serializable, Serializable> listOps = redisTemplate.opsForList();
            listOps.leftPush(key, v);
        } catch (Throwable t) {
            log.error("缓存[{}]失败, value[{}]. e:{}",k,v,t);
        }
    }


}
