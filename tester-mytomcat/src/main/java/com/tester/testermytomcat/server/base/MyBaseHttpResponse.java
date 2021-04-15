package com.tester.testermytomcat.server.base;

import com.tester.testermytomcat.server.myserver2.dto.MyHttpRequest2;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public abstract class MyBaseHttpResponse implements MyHttpResponse {

    public static final int BUFFER_SIZE = 1024;

    protected MyHttpRequest request;
    protected OutputStream output;
    protected PrintWriter writer;

    public MyBaseHttpResponse(OutputStream output){
        this.output = output;
    }

    public abstract void sendStaticResource() throws IOException;

    /**
     * call this method to send headers and response to the output
     */
    @Override
    public void finishResponse() {
        // sendHeaders();
        // Flush and close the appropriate output mechanism
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    // Response method
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
