package com.tester.testeraop.service;

import com.tester.testeraop.annotation.CacheLock;
import com.tester.testercommon.model.request.IdAndNameModel;
import org.springframework.stereotype.Service;

@Service
public class AopManager {


    @CacheLock(key = "'md:payment:card:callback:out_trade_no:'+#model.id")
    public void test_param(IdAndNameModel model) {
        System.out.println(model);
    }
}
