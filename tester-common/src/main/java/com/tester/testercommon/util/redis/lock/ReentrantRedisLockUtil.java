
package com.tester.testercommon.util.redis.lock;

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
@Slf4j
@Component("reentrantRedisLockUtil")
public class ReentrantRedisLockUtil {
    /**
     * traceId 过期时间，比主key时间长 5s
     **/
    private int traceIdAddTimeout = 1000*5;

    public static final DefaultRedisScript GET_REENTRANT_LOCK_LUA_SCRIPT = new DefaultRedisScript(
            "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then \n" +
                    "    redis.call('pexpire',KEYS[1],ARGV[2])\n" +
                    "    redis.call('INCR',KEYS[2]) \n" +
                    "    return redis.call('pexpire',KEYS[2],ARGV[3])\n" +
                    "else\n" +
                    "    if redis.call('get',KEYS[1]) == ARGV[1] then \n" +
                    "        redis.call('INCR',KEYS[2]) \n" +
                    "        return 1 \n" +
                    "    else\n" +
                    "        return 0\n" +
                    "    end\n" +
                    "end", Long.class);

    public static final DefaultRedisScript REMOVE_REENTRANT_LOCK_LUA_SCRIPT = new DefaultRedisScript(
            "" +
            "if redis.call('get',KEYS[1]) == ARGV[1] then \n" +
            "    if redis.call('DECR',KEYS[2]) < 1 then\n" +
            "        redis.call('del',KEYS[2])\n" +
            "        return redis.call('del',KEYS[1])\n" +
            "    else\n" +
            "        return 1--释放成功，但是仍然持有锁\n" +
            "    end\n" +
            "else\n" +
            "    return 1--不存在当前traceId的锁，释放成功\n" +
            "end\n", Long.class);


    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * true=成功，false=失败
     * <p>注：使用线程池技术时，需要设置上下文。@see com.tester.testerasync.config.AsyncConfig#getAsyncExecutor() -- task 装饰器</p>
     * @param key
     * @param timeout
     * @return boolean
     * @Date 17:36 2020/11/11
     * @Author 温昌营
     **/
    public boolean getLock(String key, int timeout) throws BusinessException {
        List<String> keys = new ArrayList<>();
        String traceId = addKeys(keys, key);
        log.info("keys:{}",keys);
        String traceIdTimeout = String.valueOf(traceIdAddTimeout+timeout);
        Object result = this.redisTemplate.execute(GET_REENTRANT_LOCK_LUA_SCRIPT, keys, new Object[]{traceId, String.valueOf(timeout),traceIdTimeout});
        return Integer.parseInt(String.valueOf(result)) > 0;
    }

    /**
     * 释放不判断成功失败
     * <p>注：使用线程池技术时，需要设置上下文。@see com.tester.testerasync.config.AsyncConfig#getAsyncExecutor() -- task 装饰器</p>
     * @param key
     * @return boolean
     * @Date 17:27 2020/11/11
     * @Author 温昌营
     **/
    public boolean removeLock(String key) throws BusinessException {
        List<String> keys = new ArrayList<>();
        String traceId = addKeys(keys,key);
        log.info("keys:{}",keys);
        Object result = this.redisTemplate.execute(REMOVE_REENTRANT_LOCK_LUA_SCRIPT, keys, new Object[]{traceId});
        return String.valueOf(1).equals(String.valueOf(result));
    }

    /**
     * 接入key。
     * <br/>加prefix是为了保证lua的所有操作key都在同一个slot中。
     * <br/>redis只将 : 前 {} 里面的数据做hash，所以只要保证{}里面的内容一致，就能将lua的key都hash到同一个slot
     * <p>注：使用线程池技术时，需要设置上下文。@see com.tester.testerasync.config.AsyncConfig#getAsyncExecutor() -- task 装饰器</p>
     * @param keys
     * @param dataKey
     * @return void
     * @Date 17:31 2020/11/11
     * @Author 温昌营
     **/
    private String addKeys(List<String> keys, String dataKey){
        String traceId = MDC.get("X-B3-TraceId");
//        traceId = "111";
        String prefix = "{"+dataKey+"}:";
//        prefix = "{orderNo}:";
        keys.add(prefix+dataKey);
        keys.add(prefix+traceId);
        return traceId;
    }
}
