package com.tester.testerrpc.server;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.controller.RestResult;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2021-10-18 13:54:51
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

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
                String name = Thread.currentThread().getName();
                System.out.println("name = " + name);
                long id = Thread.currentThread().getId();
                System.out.println("id = " + id);
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
                    if(requestObject.getCode() == 88){
                        return;
                    }
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

                    if ((sn = ServiceMap.getServiceNodesMap().get(methodName)) == null ||
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
}
