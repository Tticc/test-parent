package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.annotation.ReentrantCacheLock;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testercommon.util.redis.lock.RedisLockUtil;
import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testercommon.util.redis.lock.ReentrantRedisLockUtil;
import com.tester.testerwebapp.service.ReentrantLockTestManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController extends BaseController {
    @Autowired
    private RedisUtilValue redisUtilValue;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private ReentrantRedisLockUtil reentrantRedisLockUtil;
    @Autowired
    private ReentrantLockTestManager reentrantLockTestManager;


    private String key = "md:cv:orderNo1";
    private int timeout = 1000*60*5;



    @RequestMapping(value="lock", method = RequestMethod.POST)
    public Mono<RestResult<String>> lock() throws BusinessException {
        boolean lock = redisLockUtil.getLock(key, "12321", 30000);
        Mono<String> stringMono = Mono.justOrEmpty(String.valueOf(lock));
        return monoSuccess(stringMono);
    }

    @RequestMapping(value="unLock", method = RequestMethod.POST)
    public Mono<RestResult<String>> unLock() throws BusinessException {
        boolean b = redisLockUtil.removeLock(key, "12321");
        Mono<String> stringMono = Mono.justOrEmpty(String.valueOf(b));
        return monoSuccess(stringMono);
    }




    // ******************* 可重入redis锁 ************************************************************************
    @RequestMapping(value="reentrantLock", method = RequestMethod.POST)
    @ReentrantCacheLock(key = ConstantList.LOCK_DEFAULT_KEY+"#request.text")
    public Mono<RestResult<String>> reentrantLock(@RequestBody @Validated TextRequest request) throws BusinessException {
        System.out.println(this);
        reentrantLockTestManager.lock_async(request);
//        reentrantLockTestManager.lock(key,timeout);
        Mono<String> stringMono = Mono.justOrEmpty("true");
        return monoSuccess(stringMono);
    }

    @Deprecated
    @RequestMapping(value="reentrantLock0", method = RequestMethod.POST)
    public Mono<RestResult<String>> reentrantLock0() throws BusinessException {
        boolean lock = reentrantRedisLockUtil.getLock(key,timeout);
//        reentrantLockTestManager.lock_async(key,timeout);
//        reentrantLockTestManager.lock(key,timeout);
        Mono<String> stringMono = Mono.justOrEmpty(String.valueOf(lock));
        return monoSuccess(stringMono);
    }

    @RequestMapping(value="releaseReentrantLock", method = RequestMethod.POST)
    public Mono<RestResult<String>> releaseReentrantLock() throws BusinessException {
        boolean lock = reentrantRedisLockUtil.removeLock(key);
        Mono<String> stringMono = Mono.justOrEmpty(String.valueOf(lock));
        return monoSuccess(stringMono);
    }

    @RequestMapping(value="reLock", method = RequestMethod.POST)
    public RestResult<String> reLock() throws BusinessException {
        testLockMethod(0,5);
        return success();
    }




    private void testLockMethod(int count,int maxTime) throws BusinessException {
        if(count >= maxTime){
            return;
        }
        try {
            boolean lock = reentrantRedisLockUtil.getLock(key, timeout);
            if (lock) {
                System.out.println("lockSuccess。time:"+ count);
                testLockMethod(++count,maxTime);
            }
        }finally {
            reentrantRedisLockUtil.removeLock(key);
        }
    }





}
