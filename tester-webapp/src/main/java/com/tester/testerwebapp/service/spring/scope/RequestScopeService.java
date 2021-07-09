package com.tester.testerwebapp.service.spring.scope;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author 温昌营
 * @Date 2021-7-5 15:54:25
 */
@Service
@Scope(scopeName = RequestAttributes.REFERENCE_REQUEST)
public class RequestScopeService implements DisposableBean, InitializingBean {
    public void printMySelf(){
        System.out.println();
        System.out.println();
        System.out.println(this);
        System.out.println();
        System.out.println();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("come in to afterPropertiesSet");

    }
    @PostConstruct
    public void init(){
        System.out.println("come in to @PostConstruct");
    }

    @PreDestroy
    private void xxx(){
        System.out.println("come in to @PreDestroy");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("come in to destroy");
    }
}
