package com.tester.testerfuncprogram.reactor.flux;

import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

public class MySubscriber {

    public static void main(String[] args) {
        Flux.range(0,10)
                // Flux方法
                .doOnRequest(r -> System.out.println("request amount "+r))
                .limitRate(2,2)
                .limitRequest(3)
                // subscriber
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    public void hookOnSubscribe(Subscription subscription){
                        request(8);
                    }
                    @Override
                    public void hookOnNext(Integer integer){
                        System.out.println("received "+integer);
                        if(integer >= 114){
                            System.out.println("Cancelling after having received "+integer);
                            cancel();
                        }
                    }
                });


    }
}
