package com.tester.testerasync.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlTest {

    @Test
    public void test_context(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Test1 test1 = context.getBean("test1", Test1.class);
        test1.print();

    }
}
