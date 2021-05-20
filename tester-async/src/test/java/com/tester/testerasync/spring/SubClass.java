package com.tester.testerasync.spring;

public class SubClass extends SuperClass{

    public SubClass(){
        System.out.println("SubClass constructor");
    }

    static {
        System.out.println("SubClass static block");
    }
}
