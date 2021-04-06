package com.tester.testermytomcat.server.myserver2;


import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.myserver2.dto.MyHttpRequest2;
import com.tester.testermytomcat.server.myserver2.dto.MyHttpResponse2;
import com.tester.testermytomcat.server.myserver2.processor.ServletProcessor2;
import com.tester.testermytomcat.server.myserver2.processor.StaticResoureProcessor2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * servlet 容器2
 */
@Slf4j
public class MyHttpServer2 {

    private static final String SHUTDOWN_COMMAND = Constants.SHUTDOWN_COMMAND;

    private boolean shutdown = false;

    public static void main(String[] args) {
        MyHttpServer2 server = new MyHttpServer2();
        server.await();
    }

    private void await(){
        ServerSocket serverSocket = null;
        try{
            serverSocket = CommonMethod.createServerSocket();
        }catch (IOException e){
            log.error("server2 start failed.",e);
            System.exit(1);
        }
        while (!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try{
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                // 创建并解析
                MyHttpRequest2 request = new MyHttpRequest2(input);
                request.parse();

                // response
                MyHttpResponse2 response = new MyHttpResponse2(output);
                response.setRequest(request);

                // 如果是访问静态资源
                if(request.getUri().startsWith("/static")){
                    StaticResoureProcessor2 sourceProcessor = new StaticResoureProcessor2();
                    sourceProcessor.process(request, response);
                }else {
                    ServletProcessor2 servletProcessor = new ServletProcessor2();
                    servletProcessor.process(request,response);
                }
                // 关闭socket
                socket.close();
                shutdown = SHUTDOWN_COMMAND.equals(request.getUri());
            }catch (Exception e){
                log.error("server2 socket 连接建立失败。",e);
            }
        }
    }
}
