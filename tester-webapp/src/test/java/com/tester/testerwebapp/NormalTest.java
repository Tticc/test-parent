package com.tester.testerwebapp;

import org.junit.Test;

import java.util.Objects;

public class NormalTest {

    @Test
    public void test_equal(){
        int a = 125577;
        Integer b = new Integer(125577);
        System.out.println(b.equals(a));
    }
}
