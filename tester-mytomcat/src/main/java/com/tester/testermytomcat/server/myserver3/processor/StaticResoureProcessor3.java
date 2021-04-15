package com.tester.testermytomcat.server.myserver3.processor;

import com.tester.testermytomcat.server.base.MyHttpRequest;
import com.tester.testermytomcat.server.base.MyHttpResponse;

import java.io.IOException;

public class StaticResoureProcessor3 {

    public void process(MyHttpRequest request, MyHttpResponse response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
