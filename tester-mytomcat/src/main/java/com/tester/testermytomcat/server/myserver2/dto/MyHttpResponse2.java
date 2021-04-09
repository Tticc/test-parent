package com.tester.testermytomcat.server.myserver2.dto;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.base.MyBaseHttpResponse;
import com.tester.testermytomcat.server.base.MyHttpResponse;
import lombok.Data;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class MyHttpResponse2 extends MyBaseHttpResponse {

    private static final int BUFFER_SIZE = 1024;

    MyHttpRequest2 request;
    OutputStream output;
    PrintWriter writer;
    public MyHttpResponse2(OutputStream output) {
        this.output = output;
    }


    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        try{
            // todo 流读取待优化
            File file = new File(Constants.WEB_ROOT,request.getUri());
            if(file.exists()) {
                successHead();
                try (FileInputStream fis = new FileInputStream(file)){
                    int ch = fis.read(bytes, 0, BUFFER_SIZE);
                    while (ch != -1) {
                        output.write(bytes, 0, ch);
                        ch = fis.read(bytes, 0, BUFFER_SIZE);
                    }
                }catch (IOException ioe) {
                    notFound();
                }
            }else {
                notFound();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * success head
     */
    private void successHead() throws IOException {
        String successHead = CommonMethod.successHead();
        output.write(successHead.getBytes());
    }

    /**
     * file not found
     * @throws IOException
     */
    private void notFound() throws IOException {
        String errorMessage = CommonMethod.notFoundResStr();
        output.write(errorMessage.getBytes());
    }



    public void setRequest(MyHttpRequest2 request) {
        this.request = request;
    }




    /** 实现 ServletResponse 的方法  */
    /**
     * 实现获取writer
     * @return
     * @throws IOException
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        // autoflush is true, println() will flush,
        // but print() will not.
        writer = new PrintWriter(output, true);
        return writer;
    }

}
