package com.tester.testerwebapp.service.spring.scope;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @Author 温昌营
 * @Date 2021-7-5 15:54:25
 */
@Service
@Scope(scopeName = RequestAttributes.REFERENCE_SESSION)
public class SessionScopeService implements DisposableBean {
    public void printMySelf(){
        System.out.println();
        System.out.println();
        System.out.println(this);
        System.out.println();
        System.out.println();
    }

    /**
     * tomcat的sessionid自动过期时间是1800s，也就是说，1800s之内没有请求发生，session就会过期，这个方法会被调用
     * @param
     * @return void
     * @Date 17:58 2021/7/7
     * @Author 温昌营
     **/
    @Override
    public void destroy() throws Exception {
        System.out.println("come in to destroy");
    }
}
