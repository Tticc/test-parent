package com.tester.testernormaltest.list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-5-20 10:55:41
 */
public class ListTest {






    /**
     * 测试forEach遍历
     * @Date 11:05 2021/5/20
     * @Author 温昌营
     **/
    @Test
    public void test_forEach() {
        // 只会调用一次getList()，不会重复调用，应该是forEach自动做了保存
        for (Integer i : getList()) {
            System.out.println(i);
        }
        // 会重复调用getList()
        while (getList().iterator().hasNext()){
            System.out.println(getList().iterator().next());
        }
        // 明显，会重复调用getList()
        for (int i = 0; i < getList().size(); i++) {
            Integer integer = getList().get(i);
            System.out.println(integer);
        }
    }

    private List<Integer> getList(){
        ArrayList<Integer> objects = new ArrayList<>();
        objects.add(1);
        objects.add(3);
        objects.add(5);
        return objects;
    }
}
