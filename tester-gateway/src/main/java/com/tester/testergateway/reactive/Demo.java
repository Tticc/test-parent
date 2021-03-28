package com.tester.testergateway.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Demo {
    public static void main(String[] args) {

        Mono<Integer> mono = getMono();
        Flux<Integer> flux = getFlux();

        // 订阅处理数据
        flux.subscribe(e -> System.out.println(e));

    }



    private static Mono<Integer> getMono(){
        // 发送数据
        Mono<Integer> just = Mono.just(1);
        return just;
    }

    private static Flux<Integer> getFlux(){
        // 发送数据
        Flux<Integer> just = Flux.just(1, 2, 3, 4);
        return just;
    }
}
