package com.tester.testercommon.utils;

import com.tester.testercommon.util.classscanner.InstanceUtil;
import org.junit.Test;

/**
 * @Author 温昌营
 * @Date
 */
public class InstanceTest {

    @Test
    public void test_ins(){
        Object beanByFullPath = InstanceUtil.getBeanByFullPath(InstanceTest.class.getName());
        System.out.println(beanByFullPath);
    }
}
