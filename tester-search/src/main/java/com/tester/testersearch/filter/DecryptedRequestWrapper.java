package com.tester.testersearch.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DecryptedRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;
    private final Map<String, List<String>> customHeaders = new HashMap<>();

    public DecryptedRequestWrapper(HttpServletRequest request, String decryptedJson) {
        super(request);
        this.body = decryptedJson.getBytes();

        // 把 Content-Type 改为 application/json
        customHeaders.put("Content-Type", Collections.singletonList("application/json"));
    }

    // 重写 getInputStream，返回解密后的 JSON 字节流
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 不支持异步读取
            }
        };
    }

    // 重写 getReader，提供字符流读取
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    // 重写 getContentType，返回 application/json
    @Override
    public String getContentType() {
        return "application/json";
    }

    // 重写 getHeader，拦截 Content-Type 返回自定义值
    @Override
    public String getHeader(String name) {
        if ("Content-Type".equalsIgnoreCase(name)) {
            return "application/json";
        }
        if ("Accept".equalsIgnoreCase(name)) {
            return "application/json"; // 告诉 Spring 返回普通 JSON
        }
        return super.getHeader(name);
    }


    // 重写 getHeaders，拦截 Content-Type 返回自定义值
    @Override
    public Enumeration<String> getHeaders(String name) {
        if ("Content-Type".equalsIgnoreCase(name)) {
            return Collections.enumeration(Collections.singletonList("application/json"));
        }
        if ("Accept".equalsIgnoreCase(name)) {
            return Collections.enumeration(Collections.singletonList("application/json"));
        }
        return super.getHeaders(name);
    }

    // 重写 getHeaderNames，确保 Content-Type 在头列表中
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if (!names.contains("Content-Type")) {
            names.add("Content-Type");
        }
        if (!names.contains("Accept")) {
            names.add("Accept");
        }
        return Collections.enumeration(names);
    }
}
