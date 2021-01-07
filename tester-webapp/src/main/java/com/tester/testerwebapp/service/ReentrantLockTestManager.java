package com.tester.testerwebapp.service;


import com.tester.testercommon.annotation.ReentrantCacheLock;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testercommon.util.SpringBeanContextUtil;
import com.tester.testercommon.util.redis.lock.ReentrantRedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ReentrantLockTestManager{

    @Autowired
    private ReentrantRedisLockUtil reentrantRedisLockUtil;

//    @Async("cusThreadPool")
    @ReentrantCacheLock(key = ConstantList.LOCK_DEFAULT_KEY+"#request.text")
    @Transactional(value = "transactionManger-normal")
    public void lock_async(TextRequest request) throws BusinessException {
        ApplicationContext applicationContext = SpringBeanContextUtil.getApplicationContext();
        Map<String, ReentrantLockTestManager> beansOfType = applicationContext.getBeansOfType(ReentrantLockTestManager.class);
        ReentrantLockTestManager bean = applicationContext.getBean(ReentrantLockTestManager.class);
        System.out.println("here is no");
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
