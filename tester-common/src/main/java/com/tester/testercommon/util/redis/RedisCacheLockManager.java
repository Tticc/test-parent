
package com.tester.testercommon.util.redis;

import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("redisCacheLockManager")
@Slf4j
public class RedisCacheLockManager {
    public static final DefaultRedisScript REMOVE_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return -1 end", Long.class);
    public static final DefaultRedisScript GET_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end", Long.class);


    @Autowired
    private StringRedisTemplate redisTemplate;

    public RedisCacheLockManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean getLock(String key, String value, int timeout) throws BusinessException {
        List<String> keys = new ArrayList();
        keys.add(key);
        Object result = this.redisTemplate.execute(GET_LOCK_LUA_SCRIPT, keys, new Object[]{value, String.valueOf(timeout)});
        return String.valueOf(1).equals(result.toString());
    }

    public boolean removeLock(String key, String tokenVersion) throws BusinessException {
        List<String> keys = new ArrayList();
        keys.add(key);
        Object result = this.redisTemplate.execute(REMOVE_LOCK_LUA_SCRIPT, keys, new Object[]{tokenVersion});
        return String.valueOf(1).equals(result.toString());
    }
}
