package com.tester.testermytomcat.socket;

import java.io.*;
import java.net.Socket;

/**
 * @Author 温昌营
 * @Date 2021-4-2 15:20:24
 */
public class DemoSocket {

    public static void main(String[] args) throws Exception {
        startSocket();
    }

    private static void startSocket() throws Exception {
        OutputStream os = socket.getOutputStream();
        boolean autoFlush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoFlush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 发送请求
        out.println("GET / HTTP/1.1");
        out.println("Host: localhost:8080");
        out.println("Connection: Close");
        out.println();

        // 读取请求
        boolean loop = true;
        StringBuffer sb = new StringBuffer(8096);
        while (loop){
            if(in.ready()){
                int i = 0;
                while (i != -1){
                    i = in.read();
                    sb.append((char)i);
                }
                loop = false;
            }
            Thread.currentThread().sleep(50);
        }
        System.out.println(sb.toString());
        socket.close();
    }

    private static Socket socket;

    static {
        try {
            socket = new Socket("localhost",8080);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
