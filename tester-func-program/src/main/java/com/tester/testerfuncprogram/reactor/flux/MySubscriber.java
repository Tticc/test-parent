package com.tester.testerfuncprogram.reactor.flux;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class MySubscriber {

    public static void main(String[] args) {
        System.out.println("currentThreadId:"+Thread.currentThread().getId());
        Flux.range(0,10)
                // Flux方法
                .doOnRequest(r -> System.out.println("request amount "+r))
                .limitRate(2)
//                .limitRequest(3)
                // subscriber
                .subscribeOn(Schedulers.newSingle("ji"))
                .subscribe(e -> System.out.println("tid:"+Thread.currentThread().getId()+", number:"+e));
                /*.subscribe(new BaseSubscriber<Integer>() {
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
                });*/


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
