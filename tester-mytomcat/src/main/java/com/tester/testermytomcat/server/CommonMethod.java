package com.tester.testermytomcat.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;

public class CommonMethod {

    /**
     * 请求成功返回头
     * @throws IOException
     */
    public static String successHead(long len)  {
        String successHead = "HTTP/1.1 200 OK\r\n"+
                "Content-type: text/html\r\n"+
                "Content-Length: "+len+"\r\n"+
                "\r\n";
        return successHead;
    }

    /**
     * 404返回
     * @throws IOException
     */
    public static String notFoundResStr() {
        String errorMessage = "HTTP/1.1 404 File Not Found!\r\n"+
                "Content-type: text/html\r\n"+
                "Content-Length: 23\r\n"+
                "\r\n"+
                "<h1>File Not Found</h1>";
        return errorMessage;
    }
    public static StringBuffer parse(InputStream input) {
        // todo 流读取待优化
        // Read a set of characters from the socket
        StringBuffer sb = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j=0; j<i; j++) {
            sb.append((char) buffer[j]);
        }
        return sb;
    }

    /**
     * 解析请求地址
     * @param requestString
     * @return
     */
    public static String parseUri(String requestString) {
        // todo 待优化, 正则优化？
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    /**
     * 创建serverSocket
     */
    public static ServerSocket createServerSocket() throws IOException {
        return createServerSocket(8080,1,"127.0.0.1");
    }

    public static ServerSocket createServerSocket(int port, int backlog, String host) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port,backlog, InetAddress.getByName(host));
        return serverSocket;
    }
}
