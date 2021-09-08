package com.tester.testerwebapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-9-7 15:23:50
 */
public class SiftDownUpTest {

    private List<Integer> list = new ArrayList<>();



    /**
     * 暂时不知道其内在的理论是什么，但是可以实现<br/>
     * 有序、数组级快速访问、插入/删除效率高
     * <br/><br/>
     * java.util.PriorityQueue
     * @Date 15:24 2021/9/7
     * @Author 温昌营
     **/
    @Test
    public void test_sift(){
        siftUp(list.size(),5);
        siftUp(list.size(),6);
        siftUp(list.size(),3);
        siftUp(list.size(),7);
        siftUp(list.size(),2);
        siftUp(list.size(),4);
        siftUp(list.size(),10);
        siftUp(list.size(),15);
        siftUp(list.size(),20);
        siftUp(list.size(),66);
        siftUp(list.size(),81);
        siftUp(list.size(),78);
        siftUp(list.size(),6);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);
        siftDown(0);
        System.out.println(list);


    }
    private void siftUp(int index, Integer value){
        list.add(-1);
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            Integer e = list.get(parent);
            if (value.compareTo(e) >= 0)
                break;
            list.set(index, e);
            index = parent;
        }
        list.set(index, value);
    }

    private void siftDown(int index){
        Integer value = list.remove(list.size() - 1);
        int size = list.size();
        int half = size >>> 1;
        while (index < half) {
            int child = (index << 1) + 1;
            Integer c = list.get(child);
            int right = child + 1;
            if (right < size && c.compareTo(list.get(right)) > 0)
                c = list.get(child = right);
            if (value.compareTo(c) <= 0)
                break;
            list.set(index, c);
            index = child;
        }
        list.set(index, value);
    }
}
