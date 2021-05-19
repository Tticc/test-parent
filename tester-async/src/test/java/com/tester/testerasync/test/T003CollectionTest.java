package com.tester.testerasync.test;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class T003CollectionTest {


    @Test
    public void test_hashMap(){
        HashMap<String,String> map = new HashMap<>();
        map.put("1","1");
        map.put("1","2");
        map.putIfAbsent("1","3");
        System.out.println(map);

    }

    @Test
    public void test_list(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);

        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        Iterator<Integer> iterator = set.iterator();
        Integer next = iterator.next();


        HashMap<String, String> map = new HashMap<>();
        map.put("1","1");



    }
    @Test
    public void test_syncList(){
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        list.add(1);

        Set<Integer> set = Collections.synchronizedSet(new HashSet<>());

        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
    }

    @Test
    public void test_uuc(){
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList();
        list.add(1);

        CopyOnWriteArraySet<Integer> set = new CopyOnWriteArraySet<>();
        set.add(1);

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap();
        map.put("1","1");
    }
}
