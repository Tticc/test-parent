
package com.tester.testercommon.util.redis;

import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 可重入redis锁
 * @Date 9:29 2020/11/11
 * @Author 温昌营
 **/
@Component("reentrantRedisLockManager")
@Slf4j
public class ReentrantRedisLockManager {
    private int traceIdTimeout = 1000*60*10;
    public static final DefaultRedisScript REMOVE_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return -1 end", Long.class);
    public static final DefaultRedisScript GET_LOCK_LUA_SCRIPT = new DefaultRedisScript("if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end", Long.class);
    public static final DefaultRedisScript GET_REENTRANT_LOCK_LUA_SCRIPT = new DefaultRedisScript(
            "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then \n" +
                    "    redis.call('pexpire',KEYS[1],ARGV[2])\n" +
                    "    redis.call('INCR',KEYS[2]) \n" +
                    "    return redis.call('pexpire',KEYS[2],ARGV[3])\n" +
                    "else\n" +
                    "    if redis.call('get',KEYS[1]) == ARGV[1] then \n" +
                    "        return redis.call('INCR',KEYS[2]) \n" +
                    "    else\n" +
                    "        return 0\n" +
                    "    end\n" +
                    "end", Long.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public ReentrantRedisLockManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean getLock(String key, String value, int timeout) throws BusinessException {
        List<String> keys = new ArrayList();
        String traceId = MDC.get("X-B3-TraceId");
        String prefix = "{"+traceId+"}:";
//        prefix = "{orderNo}:";
        keys.add(prefix+key);
        keys.add(prefix+traceId);
        System.out.println(keys);
        Object result = this.redisTemplate.execute(GET_REENTRANT_LOCK_LUA_SCRIPT, keys, new Object[]{traceId, String.valueOf(timeout),String.valueOf(traceIdTimeout)});
        return Integer.parseInt(result.toString()) > 0;
    }

    public boolean removeLock(String key, String tokenVersion) throws BusinessException {
        List<String> keys = new ArrayList();
        keys.add(key);
        Object result = this.redisTemplate.execute(REMOVE_LOCK_LUA_SCRIPT, keys, new Object[]{tokenVersion});
        return String.valueOf(1).equals(result.toString());
    }
}
