package com.tester.testermytomcat.server.myserver2.dto;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.base.MyHttpResponse;
import lombok.Data;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

@Data
public class MyHttpResponse2 implements MyHttpResponse, ServletResponse {

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


    /**
     * success head
     */
    private void successHead(long len) throws IOException {
        String successHead = CommonMethod.successHead(len);
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




    /** 实现 ServletResponse 的方法  */
    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

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

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long length) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
