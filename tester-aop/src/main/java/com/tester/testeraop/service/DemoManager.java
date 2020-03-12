package com.tester.testeraop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service
public class DemoManager {
    @Autowired
    private DaoService daoService;

    public void testanno(Long id, String name){
        daoService.doNothing(id, name);
        System.out.println("nononono");
    }
}
