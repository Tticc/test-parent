package com.tester.testactiviti;

import com.tester.testactiviti.dao.domain.NodeModelDO;
import org.junit.Test;

import java.util.Date;

/**
 * @Author 温昌营
 * @Date
 */
public class NormalTestActiviti {

    @Test
    public void test_newDO(){
        System.out.println(new NodeModelDO());
    }
    @Test
    public void test_get(){
        System.out.println(new Date().getTime());
        System.out.println("reuse_process_key_uuid_1584953640420".length());
    }
}
