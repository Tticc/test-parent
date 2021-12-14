package com.tester.testercommon.util;

import java.util.Arrays;

/**
 * 仅仅用来记录Arrays工具的部分方法
 * @Author 温昌营
 * @Date 2021-12-7 11:18:18
 */
public class ArraysUtil {

    private <T> T[] copyOf(T[] arr, int newLen){
        return Arrays.copyOf(arr, newLen);
    }

    private <T> T[] copyOfRange(T[] arr, int skip){
        return Arrays.copyOfRange(arr, skip, arr.length);
    }

    private <T> void fill(T[] arr, T ele){
        Arrays.fill(arr,ele);
    }

}
