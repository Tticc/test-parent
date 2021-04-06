package com.tester.testermytomcat.demoserver;

import com.tester.testermytomcat.demoserver.dto.MyHttpRequest;
import com.tester.testermytomcat.demoserver.dto.MyHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MyHttpServer {

    public static final String WEB_ROOT = System.getProperty("user.dir")+ File.separator+"webroot";
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    private boolean shutdown = false;

    public static void main(String[] args) {
        MyHttpServer httpServer = new MyHttpServer();
        httpServer.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;
        int backlog = 1;
        try{
            serverSocket = new ServerSocket(port,backlog, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            log.error("server start failed.",e);
            System.exit(1);
        }
        // Loop waiting for a request
        while(!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try{
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                // 创建request对象，并解析
                MyHttpRequest request = new MyHttpRequest(input);
                request.parse();
                // 创建response对象
                MyHttpResponse response = new MyHttpResponse(output);
                response.setRequest(request);
                response.sendStaticResource();
                // 关闭socket
                socket.close();
                shutdown = SHUTDOWN_COMMAND.equals(request.getUri());
            }catch (IOException e){
                log.error("socket 连接建立失败。",e);
            }
        }
    }
}
