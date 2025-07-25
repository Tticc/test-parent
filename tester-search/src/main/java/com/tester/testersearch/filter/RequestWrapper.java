package com.tester.testersearch.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 缓存请求体内容（读一次 InputStream）
        InputStream is = request.getInputStream();
        this.body = readAllBytes(request.getInputStream());
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[4096];
        int n;
        while ((n = is.read(tmp)) != -1) {
            buffer.write(tmp, 0, n);
        }
        return buffer.toByteArray();
    }

    /**
     * 重写 getInputStream 方法，返回包装后的内容
     */
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
                // 不实现异步处理
            }
        };
    }

    /**
     * 重写 getReader 方法，提供字符流方式读取
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取原始请求体字符串（可用于日志或调试）
     */
    public String getBodyString() {
        return new String(this.body);
    }

    /**
     * 获取原始请求体字节（用于解密等场景）
     */
    public byte[] getBodyBytes() {
        return this.body;
    }
}
