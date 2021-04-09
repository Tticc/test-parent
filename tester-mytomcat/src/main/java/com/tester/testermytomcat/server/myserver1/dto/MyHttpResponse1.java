package com.tester.testermytomcat.server.myserver1.dto;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.base.MyHttpResponse;

import java.io.*;

public class MyHttpResponse1 implements MyHttpResponse {

    private static final int BUFFER_SIZE = 1024;
    MyHttpRequest1 request;
    OutputStream output;

    public MyHttpResponse1(OutputStream output){
        this.output = output;
    }
    public void setRequest(MyHttpRequest1 request) {
        this.request = request;
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

    public void sendStaticResource11() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            /* request.getUri has been replaced by request.getRequestURI */
            File file = new File(Constants.WEB_ROOT, request.getUri());
            fis = new FileInputStream(file);
      /*
         HTTP Response = Status-Line
           *(( general-header | response-header | entity-header ) CRLF)
           CRLF
           [ message-body ]
         Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
      */
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1) {
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        }
        catch (FileNotFoundException e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            output.write(errorMessage.getBytes());
        }
        finally {
            if (fis!=null)
                fis.close();
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
}
