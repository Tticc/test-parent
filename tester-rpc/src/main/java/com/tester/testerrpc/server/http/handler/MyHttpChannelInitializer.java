package com.tester.testerrpc.server.http.handler;

import com.tester.testerrpc.server.http.helper.ResponseHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.ReflectionUtil;
import org.springframework.util.StringUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;

/**
 * @Author 温昌营
 * @Date 2021-10-18 13:54:51
 */
public class MyHttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    static final Unsafe UNSAFE;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // outBound
        // ObjectEncoder 将Java Object转为ByteBuf
        ch.pipeline().addLast(new StringEncoder());
        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
        });

        // inBound
        // ObjectDecoder 将ByteBuf转为Java Object
//        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
//                ClassResolvers.cacheDisabled(null)));
        ch.pipeline().addLast(new StringDecoder());
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                AbstractReferenceCountedByteBuf b = (AbstractReferenceCountedByteBuf)msg;
//                long unsignedInt = b.getUnsignedInt(0);
//                int capacity = b.capacity();
//                System.out.println("capacity = " + capacity);
//                int i1 = b.writerIndex() - b.readerIndex();
//                System.out.println("i1 = " + i1);
//                long l = b.memoryAddress();
//                byte[] bb = new byte[i1];
//                for (int i = 0; i < bb.length; i++) {
//                    byte aByte = PlatformDependent.getByte(l + i);
//                    bb[i] = aByte;
//                }
                String string = (String)msg;
                System.out.println("\nrequest str: \n" + string);



                String[] split = string.split("\r\n");
                String key = "";
                for (String s : split) {
                    boolean contains = s.contains("Sec-WebSocket-Key");
                    if(contains){
                        key = s.split(": ")[1];
                    }
                }
                boolean isHttp = StringUtils.isEmpty(key);

                String body = "" +
                        "" +
                        "" +
                        "" +
                        "<body class=\"layui-layout-body\">\n" +
                        "<H1>xjifodjsio</H1>"+
                        "<H3>"+new Date() +"</H3>"+
                        "</body>\n" +
                        "";


                // http 请求返回头 或 websocket 请求返回头
                String resHeader = isHttp ? new String(ResponseHelper.getHttpResponseHeader()) : new String(ResponseHelper.getSocketResponseHeader(key));

                System.out.println("\nresponse header str: \n" + resHeader);
                ctx.writeAndFlush(resHeader);
                Thread.sleep(1000);
                ctx.writeAndFlush(new String(ResponseHelper.getHttpResponseBody(body)));
                ctx.writeAndFlush(new String(ResponseHelper.getEndLine()));
//                ctx.channel().close();
            }
        });
    }



    static {
        final Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                    // We always want to try using Unsafe as the access still works on java9 as well and
                    // we need it for out native-transports and many optimizations.
                    Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
                    if (cause != null) {
                        return cause;
                    }
                    // the unsafe instance
                    return unsafeField.get(null);
                } catch (NoSuchFieldException e) {
                    return e;
                } catch (SecurityException e) {
                    return e;
                } catch (IllegalAccessException e) {
                    return e;
                } catch (NoClassDefFoundError e) {
                    // Also catch NoClassDefFoundError in case someone uses for example OSGI and it made
                    // Unsafe unloadable.
                    return e;
                }
            }
        });
        UNSAFE = (Unsafe) maybeUnsafe;
    }

}
