package com.tester.testerrpc.server.socket.handler;

import com.tester.testerrpc.server.socket.handler.NioWebSocketHandler;
import com.tester.testerrpc.server.socket.log.SocketPusher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;

/**
 * websocket 服务初始化器
 * @Date 15:07 2021/11/2
 * @Author 温昌营
 **/
public class MySocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    Logger log = SocketPusher.getLog();

    @Override
    protected void initChannel(SocketChannel ch) {
        log.info("NEW SOCKET connected");
        ch.pipeline().addLast(new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast(new HttpServerCodec());//设置解码器
//        ch.pipeline().addLast(new WebSocket13FrameEncoder(false));
//        ch.pipeline().addLast(new WebSocket13FrameDecoder(WebSocketDecoderConfig.newBuilder()
//                .allowExtensions(false)
//                .maxFramePayloadLength(65536)
//                .allowMaskMismatch(false)
//                .build()));//设置解码器
        ch.pipeline().addLast(new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        ch.pipeline().addLast(new ChunkedWriteHandler());//用于大数据的分区传输
        ch.pipeline().addLast(new NioWebSocketHandler());//自定义的业务handler
    }

}

