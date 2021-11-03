package com.tester.testerrpc.server.http;

import com.tester.testerrpc.server.http.handler.MyHttpChannelInitializer;
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
 * http服务提供。<br/>
 * 浏览器输入地址：http://localhost:8000
 * <br/><br/>
 * 或者socket的方式
 * @Date 15:23 2021/11/2
 * @Author 温昌营
 **/
public class MyHttpServerProvider {

    Logger log = LoggerFactory.getLogger(MyHttpServerProvider.class);


    public static void main(String[] args) throws Exception {
        doProvide(1, 8000);
    }

    public static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 将 dubbo server放到线程池中。提供service服务。<br/>
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
                    new MyHttpServerProvider().providedOAnnotation(port);
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
        EventLoopGroup listener = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(listener, worker);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new MyHttpChannelInitializer());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            listener.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
