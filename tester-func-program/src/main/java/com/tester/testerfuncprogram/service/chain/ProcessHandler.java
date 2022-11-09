package com.tester.testerfuncprogram.service.chain;

import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2022-8-25 14:11:32
 */
public class ProcessHandler {

    private static List<MyChain> list = Arrays.asList(new OneProcessChain(),new TwoProcessChain());

    public static Mono<Void> start(Object obj) {
        System.out.println("StartProcessChain receive something: " + obj);
        // do something for myself
        if (CollectionUtils.isEmpty(list)) {
            return Mono.empty();
        }
        Iterator<MyChain> iterator = list.iterator();
        return iterator.next().filter(iterator, obj);
    }
}
