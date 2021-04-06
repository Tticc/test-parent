package com.tester.testermytomcat.demoserver.dto;

import com.tester.testermytomcat.demoserver.MyHttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyHttpResponse {

    private static final int BUFFER_SIZE = 1024;
    MyHttpRequest request;
    OutputStream output;

    public MyHttpResponse(OutputStream output){
        this.output = output;
    }
    public void setRequest(MyHttpRequest request) {
        this.request = request;
    }

    public void sendStaticResource(){
        byte[] bytes = new byte[BUFFER_SIZE];
        try{
            // todo 流读取待优化
            File file = new File(MyHttpServer.WEB_ROOT,request.getUri());
            if(file.exists()) {
                successHead(file.length());
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
    private void successHead(long len) throws IOException {
        String successHead = "HTTP/1.1 200 OK\r\n"+
                "Content-type: text/html\r\n"+
                "Content-Length: "+len+"\r\n"+
                "\r\n";
        output.write(successHead.getBytes());
    }

    /**
     * file not found
     * @throws IOException
     */
    private void notFound() throws IOException {
        String errorMessage = "HTTP/1.1 404 File Not Found!\r\n"+
                "Content-type: text/html\r\n"+
                "Content-Length: 23\r\n"+
                "\r\n"+
                "<h1>File Not Found</h1>";
        output.write(errorMessage.getBytes());
    }
}
