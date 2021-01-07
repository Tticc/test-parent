package com.tester.testercommon.util.redis.lock;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.MySupplier;

/**
 * redis锁
 * <br/>
 * 注：尽量不要在线程池技术中使用redis锁是需要慎重考虑
 * @Author 温昌营
 * @Date 2020-12-21 10:52:56
 */
public abstract class LockHelper {
    protected int defaultRetryTime = ConstantList.defaultRetryTime;
    protected int defaultInterval = ConstantList.defaultInterval;
    protected int defaultTimeout = ConstantList.defaultTimeout;

    public <T> T tryLock(String prefix, String value, MySupplier<T> supplier) throws BusinessException {
        return tryLock(prefix, value, defaultRetryTime, defaultInterval, defaultTimeout, supplier);
    }

    public <T> T tryLock(String prefix, String value, int retryTimes, MySupplier<T> supplier) throws BusinessException {
        return tryLock(prefix, value, retryTimes, defaultInterval, defaultTimeout, supplier);
    }

    public <T> T tryLock(String prefix, String value, int retryTimes, int interval, MySupplier<T> supplier) throws BusinessException {
        return tryLock(prefix, value, retryTimes, interval, defaultTimeout, supplier);
    }

    public <T> T tryLock(String prefix, String value, int retryTimes, int interval, int timeout, MySupplier<T> supplier) throws BusinessException {
        String lockKey = prefix + value;
        boolean locked = false;
        try {
            locked = tryLockSub(lockKey, value, retryTimes, interval, timeout);
            if (!locked) {
                throw new BusinessException(5008L);
            }
            return supplier.get();
        } finally {
            if (locked) {
                releaseLockSub(lockKey, value);
            }
        }
    }


    protected abstract boolean tryLockSub(String lockKey, String value, int retryTimes, int interval, int timeout);

    public void releaseLock(String prefix, String key) throws BusinessException {
        String lockKey = prefix + key;
        releaseLockSub(lockKey, key);
    }

    protected abstract void releaseLockSub(String lockKey, String key) throws BusinessException;
}
