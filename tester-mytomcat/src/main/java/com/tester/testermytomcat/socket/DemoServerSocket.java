package com.tester.testermytomcat.socket;

import io.swagger.models.auth.In;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author 温昌营
 * @Date 2021-4-2 15:56:30
 */
public class DemoServerSocket {

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws Exception {
        boolean started = true;
        while (started){
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (bufferedReader.ready()){
                String s = bufferedReader.readLine();
                System.out.println("socketServer receipt: "+s);
                if("STOP".equals(s)){
                    started = false;
                    break;
                }
            }
            PrintWriter out = new PrintWriter(accept.getOutputStream(), true);
            out.println("bye");
            Thread.currentThread().sleep(50);
        }
        serverSocket.close();
    }
    static {
        try {
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
        }
    }
}
