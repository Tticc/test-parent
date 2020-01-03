package com.tester.testerfuncprogram.reactor.flux;

import reactor.core.publisher.Flux;

public class FluxTester {

    public static void main(String[] args){
        Flux.just("hello","world").subscribe(System.out::println);
    }
}
