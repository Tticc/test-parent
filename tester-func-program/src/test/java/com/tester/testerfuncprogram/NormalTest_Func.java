package com.tester.testerfuncprogram;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;

public class NormalTest_Func {

    @Test
    public void test_dump(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if(i%1000 == 0){
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
            list.add("ajiffffffffffffffffffffffoooooooooojfioeqaiofidosajfoa;jfiosaf");
        }
    }


    @Test
    public void test_call() throws Exception {
        Mono<List<String>> listMono = Flux.just("something", "chain")
                .map(secret -> secret.replaceAll(".", "*"))
                .collectList();
//        listMono.su
//        WSDLModelerBase
    }


    @Test
    public void test_flatMap() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(0, null);
        map.put(1, new ArrayList<>(Arrays.asList(11, 12, 13, 14, 15)));
        map.put(2, new ArrayList<>(Arrays.asList(21, 22, 23, 24, 25)));
        map.put(3, null);
        map.entrySet().stream()
                .map(e -> e.getValue())
                .filter(e -> e != null)
                .flatMap(e -> e.stream())
                .forEach(System.out::println);
    }

    @Test
    public void test_jdkFunc() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(0, null);
        map.put(1, new ArrayList<>(Arrays.asList(11, 12, 13, 14, 15)));
        map.put(2, new ArrayList<>(Arrays.asList(21, 22, 23, 24, 25)));
        map.put(3, null);
        Function<Map<Integer, List<Integer>>, List<Integer>> function = (a) -> {
            List<Integer> list = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> entry : a.entrySet()) {
                if (entry.getValue() != null) {
                    list.addAll(entry.getValue());
                }
            }
            return list;
        };
        Function<List<Integer>, Integer> functionAfter1 = (a) -> {
            Integer result = 0;
            for (Integer i : a) {
                result += i;
            }
            return result;
        };
        Function<Integer, String> functionAfter2 = (a) -> {
            if (a > 490) {
                return "to large";
            }
            return "available size";
        };
        System.out.println(function.andThen(functionAfter1).andThen(functionAfter2).apply(map));
        System.out.println(Function.identity().apply(90));
//        map.entrySet().stream().map(e -> e.getValue()).reduce(new ArrayList<>(), (a, b) -> {
//            if(b != null){
//                a.addAll(b);
//            }
//            return a;
//        }).forEach(System.out::println);
//        map.entrySet().stream().map(e -> e.getValue()).reduce(new ArrayList<>(), AddAllFunction::addAll).forEach(System.out::println);
    }

    @Test
    public void test_func() {
        Function<String, String> func = (x) -> x.substring(0, 2);
        System.out.println(func.apply("1232132"));
    }
}
