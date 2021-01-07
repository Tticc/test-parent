package com.tester.testercommon.util.redis.lock;

import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 不可重入redis锁
 *
 * @Author 温昌营
 * @Date 2020-12-21 10:52:56
 */
@Component
@Slf4j
public class RedisLockHelper extends LockHelper {

    @Autowired
    private RedisLockUtil redisLockUtil;

    protected boolean tryLockSub(String lockKey, String value, int retryTimes, int interval, int timeout) {
        boolean locked = false;
        int retryCount = 0;
        try {
            while (!redisLockUtil.getLock(lockKey, value, timeout)) {
                if (retryCount >= retryTimes) {
                    throw new BusinessException(5008L);
                }
                Thread.sleep(interval);
                ++retryCount;
            }
            ;
            locked = true;
        } catch (Exception e) {
            log.error("key:{}，获取锁失败，重试{}次失败", lockKey, retryTimes);
        }
        return locked;
    }

    protected void releaseLockSub(String lockKey, String key) throws BusinessException {
        redisLockUtil.removeLock(lockKey, key);
    }
}
