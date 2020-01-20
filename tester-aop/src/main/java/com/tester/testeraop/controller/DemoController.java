package com.tester.testeraop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public String demoStart(@RequestParam("id")Long id, @RequestParam("name") String name){
        log.info("controller start here.");
        return "success";
    }

    @RequestMapping(value = "/demoMono", method = RequestMethod.POST)
    public Mono<List<Integer>> demoMono() throws InterruptedException {
        System.out.println("p tid:"+Thread.currentThread().getId());
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        Mono<List<Integer>> listMono = Flux.fromIterable(list)
                .handle((BiConsumer<? super Integer, SynchronousSink<Integer>>)(e, sink) -> {
                    System.out.println("tid:"+Thread.currentThread().getId());
                    if (e > 4) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    sink.next(e);
                })
                .subscribeOn(Schedulers.parallel())
                .collectList();
//        Thread.sleep(1000);
        System.out.println("done");
        return listMono;
    }
}
