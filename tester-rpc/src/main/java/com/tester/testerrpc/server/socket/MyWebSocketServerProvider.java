package com.tester.testerrpc.server.socket;

import com.tester.testerrpc.server.socket.handler.MySocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * netty对socket的支持案例。<br/><br/>
 * 使用Chrome的snippets测试。
 * <br/><br/><br/>
 * **************************************************


 let socket1 = function socketConnect(ip, traceId) {
 let ws = new WebSocket('ws://'+ip+':8000', "chat");
 // 打印websocket连接状态
 getState(ws);

 ws.onopen = function () {
 //当WebSocket创建成功时，触发onopen事件
 console.log("open");
 ws.send("traceId:"+traceId); //将消息发送到服务端
 };
 ws.onmessage = function (e) {
 //当客户端收到服务端发来的消息时，触发onmessage事件，参数e.data包含server传递过来的数据
 console.log("got msg: "+e.data);
 //             let textArea = $("#outpout_area");
 //             textArea.append(e.data);
 //             let scrollTop = textArea[0].scrollHeight;
 //             textArea.scrollTop(scrollTop);
 };
 ws.onclose = function (e) {
 //当客户端收到服务端发送的关闭连接请求时，触发onclose事件
 console.log("close");
 ws.close();
 console.log("socket was close!!!");
 };
 ws.onerror = function (e) {
 //如果出现连接、处理、接收、发送数据失败的时候触发onerror事件
 console.log(e);
 };
 };



 function getState(ws) {
 let kk = setInterval(function () {
 var s = ws.readyState;
 console.log(s);
 }, 3000);

 setTimeout(function () {
 clearInterval(kk);
 }, 30000);

 }


 socket1('localhost','wenc_01');




 * *************************************************
 * @Author 温昌营
 * @Date 2021-10-27 15:47:57
 */
public class MyWebSocketServerProvider {

    Logger log = LoggerFactory.getLogger(MyWebSocketServerProvider.class);


    public static void main(String[] args) throws Exception {
        doProvide(1, 8000);
    }

    public static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 将 websocket server放到线程池中。提供service服务。<br/>
     * 为了避免当前server的线程意外终止导致服务不可用，在初始化的时候将taskNum-1个任务放入等待队列，
     * 利用线程池特性，即使线程意外终止，也能立刻启动下一个线程，并重新提供服务。
     * 前提是 - 线程池永远只有一个线程！！！！！！
     *
     * @param taskNum 等待任务数
     * @param port    端口
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年8月31日
     */
    public static void doProvide(int taskNum, int port) throws Exception {
        if (taskNum <= 0) throw new Exception("taskNum必须大于0！");
        if (port <= 0 || port > 65536)
            throw new IllegalArgumentException("invalid port " + port);
        for (int i = 0; i < taskNum; i++) {
            threadPool.execute(() -> {
                try {
                    new MyWebSocketServerProvider().providedOAnnotation(port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    /**
     * 第3版。对象序列化传输数据，使用注解获取服务列表
     *
     * @param port
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年9月2日
     */
    public void providedOAnnotation(int port) throws Exception {
        if (port <= 0 || port > 65536)
            throw new IllegalArgumentException("invalid port " + port);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(worker);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new MySocketChannelInitializer());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            worker.shutdownGracefully();
        }
    }


}
