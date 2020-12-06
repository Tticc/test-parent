package com.tester.testeraop.intercepts.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 请求数据流转换保存为String
 * @Author 温昌营
 * @Date
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    private final String body;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request) {
        super(request);
        body = readByReader(request);
//        body = readByInputStream(request);
    }

    /**
     * 字符流读取数据。xxReader。
     * <br/>套一层Buffered提升性能
     * @param request
     * @return java.lang.String
     * @Date 14:58 2020/12/4
     * @Author 温昌营
     **/
    private String readByReader(HttpServletRequest request){
        StringBuilder sb = new StringBuilder("");
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return sb.toString();
    }

    /**
     * 字节流读取数据。xxInputStream
     * <br/>套一层Buffered提升性能
     * @param request
     * @return java.lang.String
     * @Date 14:58 2020/12/4
     * @Author 温昌营
     **/
    private String readByInputStream(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("");
        try (BufferedInputStream bf = new BufferedInputStream((request.getInputStream()))) {
            byte[] bytes = new byte[1024];
            while (bf.read(bytes) != -1) {
                sb.append(new String(bytes));
            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return sb.toString();
    }

    @Override
    public ServletInputStream getInputStream(){
        ServletInputStream servletInputStream = null;
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes())){
            servletInputStream = new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }
                @Override
                public boolean isReady() {
                    return false;
                }
                @Override
                public void setReadListener(ReadListener listener) {
                }
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return servletInputStream;
    }
    @Override
    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getBody(){
        return this.body;
    }
}
