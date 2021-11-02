package com.tester.testerrpc.server.socket.handler;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testerrpc.server.RpcServiceProvider;
import com.tester.testerrpc.server.socket.MyWebSocketServerProvider;
import com.tester.testerrpc.server.socket.log.SocketPusher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    Logger log = SocketPusher.getLog();


    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("收到消息："+msg);
        if (msg instanceof FullHttpRequest){
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.debug("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();

        log.debug("服务端收到：" + request);
        String traceId = parseTraceId(request);
        EventLoop eventExecutors = ctx.channel().eventLoop();

        // 开一个线程写消息 - 测试用
        MyWebSocketServerProvider.threadPool.execute(() -> {
            MDC.put(ConstantList.MDC_TRACE_ID_KEY, traceId);
            int count = 0;
            while (++count <= 10) {
                log.info("traceId:{}, info xxx_{}",traceId, count);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MDC.clear();
        });

        // 开一个线程等待消息，并写回到socket
        MyWebSocketServerProvider.threadPool.execute(() -> {
            MDC.put(ConstantList.MDC_TRACE_ID_KEY, traceId);
            BlockingDeque<String> byTraceId = SocketPusher.getByTraceId(traceId);
            try {
                while (true) {
                    String ele = byTraceId.poll(6, TimeUnit.SECONDS);
                    // 如果 30s 后仍然拉不到数据，默认为已经处理完成，直接断开连接
                    if (null == ele) {
                        close(ctx, traceId);
                        break;
                    }
                    TextWebSocketFrame tws = new TextWebSocketFrame(ele);
                    eventExecutors.execute(() -> {
                        ctx.channel().writeAndFlush(tws);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                MDC.clear();
            }
        });
    }


    /**
     * 关闭channel，socket断开
     * @Date 14:55 2021/11/2
     * @Author 温昌营
     **/
    private void close(ChannelHandlerContext ctx, String traceId){
        ctx.channel().writeAndFlush(new TextWebSocketFrame("\nCLOSED\n\n\n"));
        ctx.channel().close();
        SocketPusher.removeByTraceId(traceId);
        log.info("channel was closed. traceId:{}", traceId);
    }

    /**
     * 截取traceId
     * @Date 14:55 2021/11/2
     * @Author 温昌营
     **/
    private String parseTraceId(String text){
        return text.split(":")[1];
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        String webSocketURL = req.headers().get("Host");
        String subprotocols = req.headers().get("Sec-WebSocket-Protocol");
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://"+webSocketURL, subprotocols, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
    /**
     * 拒绝不合法的请求，并返回错误信息
     * */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        ctx.channel().close();
    }
}