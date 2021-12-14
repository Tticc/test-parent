package com.tester.testerfuncprogram.purefunc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://mp.weixin.qq.com/s/0gErQ3tjDLZuD1bYOhi0mQ
 * @Author 温昌营
 * @Date 2021-12-14 10:33:27
 */
public class Demo {
    final int count1 = 0;
    int count2 = 0;
    // 1. 不能修改全局变量！
    // 即使是对象也不能修改其属性。也就是说，如果传入list，那么只能读取list内容，不能操作list的内容

    // --------- 函数式
    public int returnA1(int a){
        return a+count1;
    }
    // --------- 非函数式
    public int returnA2(int a){
        ++count2;
        return a+count2;
    }

    // 2. 递归代替for循环！
    // --------- 函数式
    private int sumList1(int result, Integer[] arr){
        if(arr.length <=0){
            return result;
        }
        return sumList1(result + firstEle(arr), restEles(arr));
    }
    // --------- 非函数式
    private int sumList2(Integer[] arr){
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }
    private Integer firstEle(Integer[] arr){
        return arr[0];
    }
    private Integer[] restEles(Integer[] arr){
        return Arrays.copyOfRange(arr,1,arr.length);
    }


    // 3. 高阶函数
    // 这里的map方法就是高阶函数。
    // 高阶函数定义了公共的行为，并接收执行特性行为
    private void func(Integer[] arr){
        List<String> collect = Arrays.stream(arr)
                .map(e -> e + "")
                .collect(Collectors.toList());
    }

}
