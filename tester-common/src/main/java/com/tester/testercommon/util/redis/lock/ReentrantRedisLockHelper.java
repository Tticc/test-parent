package com.tester.testercommon.util.redis.lock;

import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 可重入redis锁
 * 注：在多线程技术中使用此可重入redis锁，可能与不可重入redis锁效果相同。
 * 原因：
 * <ol>
 *     <li>线程需要调度时间，可能调用方的流程结束了，新线程仍未开始。这时锁已经消失，其他线程可获取相同的锁</li>
 *     <li>在调用方流程结束前新线程开始了，此时仍然是可重入锁。</li>
 * </ol>
 * @Author 温昌营
 * @Date 2020-12-21 10:52:51
 */
@Component
@Slf4j
public class ReentrantRedisLockHelper extends LockHelper {

    @Autowired
    private ReentrantRedisLockUtil reentrantRedisLockUtil;


    protected boolean tryLockSub(String lockKey, String value, int retryTimes, int interval, int timeout) {
        boolean locked = false;
        int retryCount = 0;
        try {
            while (!reentrantRedisLockUtil.getLock(lockKey, timeout)) {
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
        reentrantRedisLockUtil.removeLock(lockKey);
    }
}
