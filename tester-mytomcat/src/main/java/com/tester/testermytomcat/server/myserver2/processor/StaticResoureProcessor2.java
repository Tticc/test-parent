package com.tester.testermytomcat.server.myserver2.processor;

import com.tester.testermytomcat.server.base.MyHttpRequest;
import com.tester.testermytomcat.server.base.MyHttpResponse;

import java.io.IOException;

public class StaticResoureProcessor2 {

    public void process(MyHttpRequest request, MyHttpResponse response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
