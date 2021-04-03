package com.tester.testerwebapp.service;

import com.tester.testerwebapp.service.duplicatedService.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service
public class MyService {

    @Autowired
    private ServiceB myServiceB;

    public void print(){
        System.out.println("print A");
    }

}
