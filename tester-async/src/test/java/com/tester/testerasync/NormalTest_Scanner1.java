package com.tester.testerasync;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalTest_Scanner1 {

    // 3 4
    // 0 0 1 0
    // 0 1 1 1
    // 0 0 1 0

    /**
     * 计算岛屿面积。1 代表土地，0 代表海水。往上下左右搜索，搜到 1 则面积 +1。找到最大面积的岛屿
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] s1 = s.split(" ");
        int height = Integer.valueOf(s1[0]);
        int width = Integer.valueOf(s1[1]);
        String[][] arr = new String[height][width];
        for (int i = 0; i < height; i++) {
            arr[i] = sc.nextLine().split(" ");
        }
        int max = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                AtomicInteger tempMax = new AtomicInteger(0);
                findNex(arr,i,j,tempMax);
                if(tempMax.get() > max){
                    max = tempMax.get();
                }
            }
        }
        System.out.println(max);
    }

    private static void findNex(String[][] arr, int h, int w, AtomicInteger size){
        if(h < 0 || w < 0 || h >= arr.length || w >= arr[0].length){
            return ;
        }
        if(Objects.equals(arr[h][w],"1")){
            size.incrementAndGet();
            arr[h][w] = ".";
        }else{
            return;
        }
        findNex(arr,h+1,w,size);
        findNex(arr,h-1,w,size);
        findNex(arr,h,w+1,size);
        findNex(arr,h,w-1,size);
    }
}