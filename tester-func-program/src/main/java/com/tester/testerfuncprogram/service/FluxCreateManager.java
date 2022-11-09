package com.tester.testerfuncprogram.service;

import com.tester.testerfuncprogram.service.chain.MyChain;
import com.tester.testerfuncprogram.service.chain.OneProcessChain;
import com.tester.testerfuncprogram.service.chain.ProcessHandler;
import com.tester.testerfuncprogram.service.chain.TwoProcessChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.*;

public class FluxCreateManager {
    public static void main(String[] args) throws InterruptedException {
//        test_fluxCreate_create_already();
//        test_fluxCreate_create();
        test_flux_chain();

    }

    public static void test_fluxCreate_create_already(){
        List<ActionListener> listeners = new ArrayList<>();
        listeners.add((e) -> {
                System.out.println(e);
        });
        listeners.add((e) -> {
            System.out.println(e.getActionCommand());
        });

        Flux.create(sink -> {
            for (ActionListener item : listeners) {
                sink.next(item);
            }
        }).subscribe();

        listeners.forEach(action -> action.actionPerformed(
                new ActionEvent(FluxCreateManager.class, 1, "open happens"))
        );
    }
    public static void test_fluxCreate_create(){
        List<ActionListener> listeners = new ArrayList<>();

        Flux.create(sink -> {
            listeners.add(evt -> {
                System.out.println("listener1。event command is: " + evt.getActionCommand()+" id is: "+evt.getID());
                sink.next(evt);
            });
            listeners.add(evt -> {
                System.out.println("listener2。event command is: " + evt.getActionCommand()+" id is: "+evt.getID());
                sink.next(evt);
            });
        }).subscribe(evt -> eventAction((ActionEvent)evt));

//        listeners.forEach(action -> action.actionPerformed(
//                new ActionEvent(FluxCreateManager.class, 1, "open happens"))
//        );
        int id = 1;
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(FluxCreateManager.class, id++, "open happens"));
        }
    }
    public static void eventAction(ActionEvent evt){
        System.out.println(evt.paramString());
    }

    public static void test1(){
        Mono<String> a = Mono.defer(() -> Mono.just("a").map(e -> "one__"+e));
//        Mono<String> compose = a.compose((e) -> Mono.just("b"));
        a = a.doOnSuccess(s -> System.out.println(s));
        Mono<String> compose = a.compose((e) -> e.doOnSuccess(s -> System.out.println(s)));
        Mono<String> b = compose.then(Mono.defer(() -> Mono.just("b").map(e -> "one__" + e)));
        System.out.println(b.block());

    }
    public static void test2(){
        Mono<String> a = Mono.defer(() -> Mono.just("a").map(e -> "one__"+e));
        a = a.compose((e) -> e.doOnSuccess(s -> System.out.println(s)));
        Mono<String> b = a.then(Mono.defer(() -> Mono.just("b").map(e -> "one__" + e)));
        System.out.println(b.block());
    }
    public static void testx() throws InterruptedException {
        Flux<String> stringFlux1 = Flux.just("a","b","c","d","e","f","g","h","i");
        Flux<Flux<String>> stringFlux2 = stringFlux1.window(2);
        stringFlux2.concatMap(flux1 ->flux1.map(word ->word.toUpperCase())
                .delayElements(Duration.ofMillis(200)))
                .subscribe(x -> System.out.print("->"+x));
        Thread.sleep(2000);
    }
    public static void testxxx() throws InterruptedException {
        List<String> lis = Arrays.asList("a","b","c","d","e","f","g","h","i","i","i","i");
        Flux.fromIterable(lis)
                .flatMap(e -> Mono.just(e).map(el -> el.toUpperCase()))
                .subscribe(x -> System.out.print("->"+x));
        Thread.sleep(2000);
    }


    public static void test_flux_chain(){
        Mono<Void> jojo = ProcessHandler.start("jojo");
        jojo.subscribe(e -> System.out.println(e));
    }
    public static Mono<Void> process(Iterator<Object> ite, Object obj){
        Mono<Void> res;
        try{
            res = Mono.defer(() -> {
                String s = obj.toString();
                return someOtherProcess(ite, obj, s);
            });
        }catch (Exception e){
            res = Mono.error(e);
        }
        return res;
    }

    public static Mono<Void> someOtherProcess(Iterator<Object> ite, Object obj, String s){
        if(ite.hasNext()){
            Object next = ite.next();
        }
        return Mono.empty();
    }


}
