package com.tester.testernormaltest.system;

import org.junit.Test;

/**
 * @Author 温昌营
 * @Date 2021-5-20 11:07:52
 */
public class SystemTest {



    @Test
    public void test_identityHashCode(){
        SystemTest systemTest = new SystemTest();
        int i = systemTest.hashCode();
        System.out.println("调用重写的hashCode方法结果："+i);
        int i1 = System.identityHashCode(systemTest);
        System.out.println("System.identityHashCode: "+i1);
        System.out.println("\t可见System.identityHashCode会忽略重写的hashCode方法。");
    }

    @Override
    public int hashCode(){
        return 22;
    }
}
