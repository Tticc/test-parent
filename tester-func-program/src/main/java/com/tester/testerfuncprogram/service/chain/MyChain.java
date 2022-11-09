package com.tester.testerfuncprogram.service.chain;

import reactor.core.publisher.Mono;

import java.util.Iterator;

/**
 * @Date 14:11 2022/8/25
 * @Author 温昌营
 **/
public interface MyChain {

    Mono<Void> filter(Iterator<MyChain> ite, Object obj);


    default Mono<Void> doNext(Iterator<MyChain> ite, Object obj){
        if (ite.hasNext()) {
            return ite.next().filter(ite, obj);
        } else {
            // 完成返回
            return Mono.empty();
        }
    }
}
