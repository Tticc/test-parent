package com.tester.testersearch.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ByteArrayResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public ByteArrayResponseWrapper(HttpServletResponse response) {
        super(response);
        outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        };
        writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBytes() {
        writer.flush(); // flush Writer contents
        return baos.toByteArray();
    }

    public String getBodyAsString() {
        return new String(getBytes(), StandardCharsets.UTF_8);
    }
}


