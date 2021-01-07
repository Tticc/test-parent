package com.tester.testercv;

import org.junit.Test;

import java.lang.reflect.Method;

public class LambdaTest1 {

    @Test
    public void test_methods(){
        LambdaTest1 lambdaTest1 = new LambdaTest1();
        Method[] declaredMethods = lambdaTest1.getClass().getDeclaredMethods();
        Class<?>[] declaredClasses = lambdaTest1.getClass().getDeclaredClasses();
        System.out.println(declaredMethods.length);
    }
    // 0.打开cmd
    // 1.javac LambdaTest.java
    // 2.javap -p -v -c LambdaTest.class
    // 3.java -Djdk.internal.lambda.dumpProxyClasses LambdaTest
    public void printString(String s, Print<String> print) {
        print.print(s);
    }

    public void callPrint(){
        printString("test", (x)->System.out.println(x));
    }
}

@FunctionalInterface
interface Print<T> {
    public void print(T x);
}
