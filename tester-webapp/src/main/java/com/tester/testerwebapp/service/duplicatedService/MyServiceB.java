package com.tester.testerwebapp.service.duplicatedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service
public class MyServiceB implements ServiceB {

    @Autowired
    private YouServiceB youServiceB;

    public void print(){
        System.out.println("print B");
    }
}
