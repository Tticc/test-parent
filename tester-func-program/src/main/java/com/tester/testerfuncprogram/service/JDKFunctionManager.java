package com.tester.testerfuncprogram.service;

import java.util.*;
import java.util.function.*;

public class JDKFunctionManager {
    /**
     * BiFunction
     */
    public static class BiFunctionTest {
        private static void giveParamsAndStartMethod(BiFunction<Integer, Integer, Integer> func){
            int a = 3, b =3;
            System.out.println("BiFunctionTest:"+func.apply(a,b));
        }
        // call this
        public static void giveMethod(){
            giveParamsAndStartMethod((a,b) -> a+b);
        }
    }

    public static class ConsumerTest{
        private static void giveParamsAndStartMethod(Consumer<Integer> func){
            int a = 3;
            func.accept(a);
        }
        public static void giveMethod(){
            giveParamsAndStartMethod((a) -> {
                System.out.println("ConsumerTest:"+ a);
            });
        }
    }
    public static class SupplierTest{
        private static void giveParamsAndStartMethod(Supplier<Integer> func){
            int a = 3;
            System.out.println("SupplierTest:"+func.get());
        }
        public static void giveMethod(){
            int a = 3;
            giveParamsAndStartMethod(() -> a);
        }
    }
    public static class PredicateTest{
        private static void giveParamsAndStartMethod(Predicate<Integer> func){
            int a = 3;
            System.out.println("PredicateTest:"+func.test(a));
        }
        public static void giveMethod(){
            int a = 3;
            giveParamsAndStartMethod((e) -> Objects.equals(e,a));
        }
    }


    public static void main(String[] args){
        /*Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(0,null);
        map.put(1, new ArrayList<>(Arrays.asList(11, 12, 13, 14, 15)));
        map.put(2, new ArrayList<>(Arrays.asList(21, 22, 23, 24, 25)));
        map.put(3, null);
        andThen(map);
        identity();*/
        BiFunctionTest.giveMethod();
        ConsumerTest.giveMethod();
        SupplierTest.giveMethod();
        PredicateTest.giveMethod();

    }
    /** identity。返回自身*/
    private static void identity(){
        // Function.identity()返回一个Function的实例，identity()实际上是在定义apply函数的内容。
        System.out.println(Function.identity().apply(90));
    }
    /** andThen。链式调用*/
    private static void andThen(Map<Integer, List<Integer>> map){
        // 第一个调用
        Function<Map<Integer, List<Integer>>, List<Integer>> function = (a) -> {
            List<Integer> list = new ArrayList<>();
            for (Map.Entry<Integer,List<Integer>> entry : a.entrySet()) {
                if(entry.getValue() != null){
                    list.addAll(entry.getValue());
                }
            }
            return list;
        };
        // 第二个调用
        Function<List<Integer>, Integer> functionAfter1 = (a) -> {
            Integer result = 0;
            for (Integer i : a) {
                result += i;
            }
            return result;
        };
        // 第三个调用
        Function<Integer, String> functionAfter2 = (a) -> {
            if (a > 490) {
                return "to large";
            }
            return "available size";
        };
        // 执行调用
        System.out.println(function.andThen(functionAfter1).andThen(functionAfter2).apply(map));
    }
}
