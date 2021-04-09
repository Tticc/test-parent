package com.tester.testermytomcat.server.myserver1.dto;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.base.MyBaseHttpRequest;

import java.io.InputStream;

public class MyHttpRequest1 extends MyBaseHttpRequest {

    private InputStream input;
    private String uri;
    public MyHttpRequest1(InputStream input) {
        this.input = input;
    }

    public void parse(){
        StringBuffer request = CommonMethod.parse(input);
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    private String parseUri(String requestString){
        return CommonMethod.parseUri(requestString);
    }



    public String getUri() {
        return uri;
    }



}
