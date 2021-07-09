package com.tester.testerwebapp.service.spring.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date 2021-7-5 15:54:25
 */
@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeScopeService {
    public void printMySelf(){
        System.out.println();
        System.out.println();
        System.out.println(this);
        System.out.println();
        System.out.println();
    }
}
