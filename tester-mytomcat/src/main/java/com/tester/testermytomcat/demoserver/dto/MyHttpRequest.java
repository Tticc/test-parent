package com.tester.testermytomcat.demoserver.dto;

import java.io.IOException;
import java.io.InputStream;

public class MyHttpRequest {

    private InputStream input;
    private String uri;
    public MyHttpRequest(InputStream input) {
        this.input = input;
    }

    public void parse(){

        // todo 流读取待优化
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try{
            i = input.read(buffer);
        }catch (IOException e){
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char)buffer[j]);
        }
        System.out.println(request.toString());
        uri = parseUri(request.toString());
    }

    private String parseUri(String requestString){
        // todo 待优化, 正则优化？
        int index1,index2;
        index1 = requestString.indexOf(" ");
        if(index1 != -1){
            index2 = requestString.indexOf(" ",index1+1);
            if(index2 > index1){
                return requestString.substring(index1 + 1,index2);
            }
        }
        return null;
    }



    public String getUri() {
        return uri;
    }
}
