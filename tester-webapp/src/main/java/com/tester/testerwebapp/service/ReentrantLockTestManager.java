package com.tester.testerwebapp.service;


import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.redis.ReentrantRedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReentrantLockTestManager {

    @Autowired
    private ReentrantRedisLockUtil reentrantRedisLockUtil;

    @Async("cusThreadPool")
    public void lock_async(String key, Integer timeout) throws BusinessException {
        boolean lock = false;
        try{
            lock = reentrantRedisLockUtil.getLock(key,timeout);
        }catch (Exception e){

        }finally {
            if(lock){
//                reentrantRedisLockUtil.removeLock(key);
            }
        }
    }

    public void lock(String key, Integer timeout) throws BusinessException {

        boolean lock = false;
        try{
            lock = reentrantRedisLockUtil.getLock(key,timeout);
        }catch (Exception e){

        }finally {
            if(lock){
                reentrantRedisLockUtil.removeLock(key);
            }
        }
    }
}
