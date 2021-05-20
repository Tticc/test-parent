package com.tester.testerasync.spring;

public abstract class SuperClass {

    SuperClass(){
        System.out.println("super constructor");
    }

    static {
        System.out.println("super static block");
    }
}
