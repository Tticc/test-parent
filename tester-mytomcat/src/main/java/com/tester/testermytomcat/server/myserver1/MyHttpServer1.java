package com.tester.testermytomcat.server.myserver1;

import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.myserver1.dto.MyHttpRequest1;
import com.tester.testermytomcat.server.myserver1.dto.MyHttpResponse1;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单http服务器。
 * <br/>
 *
 * <ol>使用
 *     <li>启动main方法</li>
 *     <li>浏览器访问URL：localhost:8080/index.html 或404地址 localhost:8080/fdsaefdsa  </li>
 *     <li>关闭： localhost:8080/SHUTDOWN</li>
 * </ol>
 * <br/>
 *
 * <br/>
 *
 * <br/>
 */
@Slf4j
public class MyHttpServer1 {

    private static final String SHUTDOWN_COMMAND = Constants.SHUTDOWN_COMMAND;
    private boolean shutdown = false;

    public static void main(String[] args) {
        MyHttpServer1 httpServer = new MyHttpServer1();
        httpServer.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;
        int backlog = 1;
        try{
            serverSocket = new ServerSocket(port,backlog, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            log.error("server1 start failed.",e);
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
                MyHttpRequest1 request = new MyHttpRequest1(input);
                request.parse();
                // 创建response对象
                MyHttpResponse1 response = new MyHttpResponse1(output);
                response.setRequest(request);
                response.sendStaticResource();
                // 关闭socket
                socket.close();
                shutdown = SHUTDOWN_COMMAND.equals(request.getUri());
            }catch (IOException e){
                log.error("server1 socket 连接建立失败。",e);
            }
        }
    }
}
