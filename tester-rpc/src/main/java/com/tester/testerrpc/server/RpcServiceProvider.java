package com.tester.testerrpc.server;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.controller.RestResult;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应该使用单例。<br/>
 * 接受的json串格式为：
 * 返回的json串格式为：
 *
 * @author Wen, Changying
 * 2019年8月20日
 */
public class RpcServiceProvider {

    public static void main(String[] args) throws Exception {
        doProvide(1, 8000);
    }

    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);

    /**
     * 将 dubbo server放到线程池中。提供service服务。<br/>
     * 为了避免当前server的线程意外终止导致服务不可用，在初始化的时候将taskNum-1个任务放入等待队列，
     * 利用线程池特性，即使线程意外终止，也能立刻启动下一个线程，并重新提供服务。
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
                    new RpcServiceProvider().providedOAnnotation(port);
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
        System.out.println("service provided on port " + port);
        EventLoopGroup listener = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(listener, worker);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // outBound
                    // ObjectEncoder 将Java Object转为ByteBuf
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                    });

                    // inBound
                    // ObjectDecoder 将ByteBuf转为Java Object
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            RestResult result = null;
                            ServiceMap.ServiceNode sn = null;
                            Object service = null;
                            Method m = null;

                            Map<String, Object> resultMap = new HashMap<String, Object>();
                            resultMap.put("requestData", msg);
                            try {

                                /**
                                 * 待测试项
                                 * 1.Object req = requestObject.getData();拿到的是String还是JSONObject？ 是String
                                 * 2.有参方法的调用和无参方法的调用可以兼容吗？ 可以兼容
                                 * 3.缺少参数的话，能正确提示吗？ 可以
                                 * 4.client需要对应修改调用失败的代码返回。已经改好
                                 */
                                RestResult requestObject = (RestResult) msg;
                                System.out.println("input: " + requestObject.toString());
                                Object req = requestObject.getData(); // 得到一个String，所以不需要做null判断。是吗？
                                if (req instanceof String) {
                                    System.out.println("this is a String");
                                }
                                if (req instanceof JSONObject) {
                                    System.out.println("this is a JSONObject");
                                }
                                JSONObject obj = JSONObject.parseObject(req.toString());
                                String methodName = String.valueOf(obj.get("serviceKey"));
                                if ("".equals(methodName) || "null".equals(methodName))
                                    throw new Exception("未提供serviceKey.");

                                if ((sn = ServiceMap.serviceNodesMap.get(methodName)) == null ||
                                        (m = sn.getMethod()) == null ||
                                        (service = sn.getSerivceImpl()) == null)
                                    throw new Exception("没有找到对于serviceKey:" + methodName + "对应的service方法,请更正serviceKey!");
//								Method m = ServiceMap.serviceMap.get(methodName);
//								if(m == null) 
                                Parameter[] paramName = m.getParameters();
                                // 当前方法无参。需要if吗？不需要，可以兼容
//								if(paramName != null && paramName.length == 0) {
//									result = ResultVOUtil.success(generateResultMap(obj,m.invoke(new ServiceImpl())));
//								}else {
                                Map<String, Object> params = (Map<String, Object>) obj.get("params");
                                Class<?>[] paramClass = m.getParameterTypes();
                                Object[] param = new Object[paramName.length];
                                for (int i = 0; i < paramName.length; i++) {
                                    Object paramObject = params.get(paramName[i].getName());
                                    if (paramObject == null) {
                                        throw new Exception("缺少参数：" + paramName[i].getName());
                                    }
                                    param[i] = paramObject;
                                }
                                // 正常返回
                                //result = ;
                                //resultMap.put("responseData", m.invoke(new ServiceImpl(), param));
                                resultMap.put("responseData", m.invoke(service, param));
                                result = RestResult.success(resultMap);
//								}
                            } catch (Exception e) {
                                resultMap.put("responseData", e.getMessage());
                                result = RestResult.fail(e.getMessage(), resultMap);
                            }
                            ctx.writeAndFlush(result);
                        }
                    });
                }
            });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            listener.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 第2版。对象序列化传输数据
     *
     * @param service
     * @param port
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年9月2日
     */
    public void providedO(final Object service, int port) throws Exception {
        if (service == null)
            throw new IllegalArgumentException("service instance == null");
        if (port <= 0 || port > 65536)
            throw new IllegalArgumentException("invalid port " + port);
        System.out.println("service " + service.getClass().getName() + " provided on port " + port);
        EventLoopGroup listener = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(listener, worker);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // outBound
                    // ObjectEncoder 将Java Object转为ByteBuf
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {

                    });

                    // inBound
                    // ObjectDecoder 将ByteBuf转为Java Object
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            RestResult result = null;
                            Map<String, Object> resultMap = new HashMap<String, Object>();
                            resultMap.put("requestData", msg);
                            try {

                                /**
                                 * 待测试项
                                 * 1.Object req = requestObject.getData();拿到的是String还是JSONObject？ 是String
                                 * 2.有参方法的调用和无参方法的调用可以兼容吗？ 可以兼容
                                 * 3.缺少参数的话，能正确提示吗？ 可以
                                 * 4.client需要对应修改调用失败的代码返回。已经改好
                                 */
                                RestResult requestObject = (RestResult) msg;
                                System.out.println("input: " + requestObject.toString());
                                Object req = requestObject.getData(); // 得到一个String，所以不需要做null判断。是吗？
                                if (req instanceof String) {
                                    System.out.println("this is a String");
                                }
                                if (req instanceof JSONObject) {
                                    System.out.println("this is a JSONObject");
                                }
                                JSONObject obj = JSONObject.parseObject(req.toString());
                                String methodName = String.valueOf(obj.get("serviceKey"));
                                if ("".equals(methodName) || "null".equals(methodName))
                                    throw new Exception("未提供serviceKey.");


                                Method m = ServiceMap.serviceMap.get(methodName);
                                Parameter[] paramName = m.getParameters();
                                // 当前方法无参。需要if吗？不需要，可以兼容
//								if(paramName != null && paramName.length == 0) {
//									result = ResultVOUtil.success(generateResultMap(obj,m.invoke(new ServiceImpl())));
//								}else {
                                Map<String, Object> params = (Map<String, Object>) obj.get("params");
                                Class<?>[] paramClass = m.getParameterTypes();
                                Object[] param = new Object[paramName.length];
                                for (int i = 0; i < paramName.length; i++) {
                                    Object paramObject = params.get(paramName[i].getName());
                                    if (paramObject == null) {
                                        throw new Exception("缺少参数：" + paramName[i].getName());
                                    }
                                    param[i] = paramObject;
                                }
                                // 正常返回
                                //result = ;
                                //resultMap.put("responseData", m.invoke(new ServiceImpl(), param));
                                resultMap.put("responseData", m.invoke(service, param));
                                result = RestResult.success(resultMap);
//								}
                            } catch (Exception e) {
                                resultMap.put("responseData", e.getMessage());
                                result = RestResult.fail(e.getMessage(), resultMap);
                            }
                            ctx.writeAndFlush(result);
                        }
                    });
                }
            });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            listener.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 第1版，使用字符串传输数据。StringDecoder,StringEncoder
     *
     * @param service
     * @param port
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年8月22日
     */
    @Deprecated
    public void provide(final Object service, int port) throws Exception {
        if (service == null)
            throw new IllegalArgumentException("service instance == null");
        if (port <= 0 || port > 65536)
            throw new IllegalArgumentException("invalid port " + port);
        System.out.println("service " + service.getClass().getName() + " provided on port " + port);
        EventLoopGroup listener = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(listener, worker);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // outBound
                    // StringEncoder 将String转为ByteBuf
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {

                    });

                    // inBound
                    // StringDecoder 将ByteBuf转为String
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            RestResult result = null;
                            JSONObject obj = new JSONObject();
                            try {
                                System.out.println("input: " + msg.toString());
                                obj = (JSONObject) JSONObject.parse(msg.toString());
                                String methodName = String.valueOf(obj.get("serviceKey"));
                                Map<String, Object> params = (Map<String, Object>) obj.get("params");
                                Method m = ServiceMap.serviceMap.get(methodName);
                                Parameter[] paramName = m.getParameters();
                                Class<?>[] paramClass = m.getParameterTypes();
                                Object[] param = new Object[paramName.length];
                                for (int i = 0; i < paramName.length; i++) {
                                    Object paramObject = params.get(paramName[i].getName());
                                    if (paramObject == null) {
                                        //这里抛异常，直接返回失败信息。
                                    }
                                    param[i] = paramObject;
                                }
                                result = RestResult.success(m.invoke(service, param));
                            } catch (Exception e) {
                                result = RestResult.fail(e.getMessage());
                            }
                            String rr = JSONObject.toJSON(result).toString();
                            obj.put("result", rr);
                            ctx.writeAndFlush(obj.toString());
                        }
                    });
                }
            });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            listener.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
