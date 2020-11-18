package com.tester.testerwebapp;

public class SingleTest {
    static int a;
    public static void main(String[] args) {
        System.out.println("hello,world");
        byte[] placeholder = new byte[64 * 1024 * 1024];
        placeholder=null;
//        Integer a = 100;
//        a = null;
        int b;
        System.out.println(a);
//        System.out.println(b);
        System.gc();

    }
}


