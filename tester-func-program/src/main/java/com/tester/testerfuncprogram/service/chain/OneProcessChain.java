package com.tester.testerfuncprogram.service.chain;

import reactor.core.publisher.Mono;

import java.util.Iterator;

/**
 * @Author 温昌营
 * @Date 2022-8-25 14:11:32
 */
public class OneProcessChain implements MyChain {
    @Override
    public Mono<Void> filter(Iterator<MyChain> ite, Object obj) {
        return Mono.defer(() -> {
            try {
                // do something for myself
                System.out.println("OneProcessChain receive something: " + obj);
                return doNext(ite, obj);
            }catch (Exception e){
                return Mono.error(e);
            }
        });
    }
}
