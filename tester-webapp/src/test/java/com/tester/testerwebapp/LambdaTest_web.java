package com.tester.testerwebapp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tester.base.dto.model.response.KVResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author 温昌营
 * @Date 2022-3-30 16:56:09
 */
public class LambdaTest_web {

    @Test
    public void test_reduce() {
        String str = "c d a bb e";
        String[] s = str.split(" ");
        List<String> collect = Stream.of(s).sorted().collect(Collectors.toList());
        System.out.println(collect);

        String s1 = Stream.of(s).sorted().reduce("", (a, b) -> a + "," + b);
        System.out.println("s1 = " + s1);
    }

    @Test
    public void test_toMap() {
        List<KVResponse<String, String>> list = new ArrayList<>();
        KVResponse<String, String> kv = null;
        kv = new KVResponse<>();
        kv.setKey("a").setValue("a1");
        list.add(kv );
        kv = new KVResponse<>();
        kv.setKey("a").setValue("a2");
        list.add(kv );

        kv = new KVResponse<>();
        kv.setKey("b").setValue("b");
        list.add(kv );

        kv = new KVResponse<>();
        kv.setKey("c").setValue("c");
        list.add(kv );

        Map<String, KVResponse<String, String>> collect = list.stream()
                // 冲突取value1
                .collect(Collectors.toMap(KVResponse::getKey, value -> value, (value1, value2) -> value1));
                // 冲突取value2
//                .collect(Collectors.toMap(KVResponse::getKey, value -> value, (value1, value2) -> value2));
        collect.entrySet().stream().forEach(e -> System.out.println(e.getValue()));


        Map<String, List<KVResponse<String, String>>> collect1 = list.stream()
                .collect(Collectors.toMap(KVResponse::getKey, e -> {
                    ArrayList<KVResponse<String, String>> valueList = new ArrayList<>();
                    valueList.add(e);
                    return valueList;
                }, (List<KVResponse<String, String>> list1, List<KVResponse<String, String>> list2) -> {
                    list1.addAll(list2);
                    return list1;
                }));
        System.out.println("collect1 = " + collect1);

    }

    @Test
    public void test_json(){
        KVResponse<String, String> item = new KVResponse<String, String>();
        item.setKey("1").setValue("one");
        String json = JSON.toJSONString(item);
        KVResponse<Integer, String> res1 = JSON.parseObject(json, KVResponse.class);
        System.out.println(res1);

        KVResponse<Integer, String> res2 = JSON.parseObject(json, new TypeReference<KVResponse<Integer, String>>() {
        });
        System.out.println(res2);

    }
}
