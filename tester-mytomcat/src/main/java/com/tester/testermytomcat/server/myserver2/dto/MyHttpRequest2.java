package com.tester.testermytomcat.server.myserver2.dto;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.base.MyBaseHttpRequest;
import com.tester.testermytomcat.server.base.MyHttpRequest;
import lombok.Getter;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@Getter
public class MyHttpRequest2 extends MyBaseHttpRequest {


    private InputStream input;
    private String uri;
    public MyHttpRequest2(InputStream input){
        this.input = input;
    }

    private String parseUri(String requestString){
        return CommonMethod.parseUri(requestString);
    }

    public void parse(){
        StringBuffer request = CommonMethod.parse(input);
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

}
