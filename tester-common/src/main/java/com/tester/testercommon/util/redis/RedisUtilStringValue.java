package com.tester.testercommon.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component("redisUtilStringValue")
@Slf4j
public class RedisUtilStringValue {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 前缀
     */
    public static final String KEY_PREFIX_VALUE = "tester:redis:::value::";

    public boolean setValue(String k, String v) {
        return setValue(k,v,-1);
    }

    public boolean setValue(String k, String v, long time) {
        return setValue(k,v,time,TimeUnit.SECONDS);
    }

    /**
     *
     * @param k key
     * @param v value
     * @param time 过期时间
     * @param unit 过期时间单位
     * @return
     */
    public boolean setValue(String k, String v, long time, TimeUnit unit) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            stringRedisTemplate.opsForValue().set(key, v);
            if (time > 0) {
                stringRedisTemplate.expire(key, time, unit);
            }
            return true;
        }catch (Throwable t) {
            log.error("缓存[{}]失败, value[{}]. e:{}",k,v,t);
        }
        return false;
    }

    /**
     * 判断缓存是否存在
     * @param k key
     * @return
     */
    public boolean containsValueKey(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            return stringRedisTemplate.hasKey(key);
        }catch (Throwable t) {
            log.error("判断缓存[{}]存在失败。{}",key, t);
        }
        return false;
    }

    /**
     * 获取缓存
     * @param k key
     * @return
     */
    public Serializable getValue(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            return stringRedisTemplate.opsForValue().get(key);
        }catch (Throwable t) {
            log.error("获取缓存[{}]失败。{}",key,t);
        }
        return null;
    }

    /**
     * 删除缓存
     * @param k key
     * @return
     */
    public boolean deleteValue(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            stringRedisTemplate.delete(key);
            return true;
        }catch(Throwable t) {
            log.error("获取缓存[{}]失败。{}",key,t);
        }
        return false;
    }
}
