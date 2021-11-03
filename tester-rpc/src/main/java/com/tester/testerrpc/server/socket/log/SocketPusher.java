package com.tester.testerrpc.server.socket.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * use: SocketPusher.slog.info("info xxx");
 * @Author 温昌营
 * @Date 2021-10-28 11:24:18
 */
public class SocketPusher {

    private static ConcurrentHashMap<String, BlockingDeque<String>> eventMap = new ConcurrentHashMap<>();

    private static Logger slog = LoggerFactory.getLogger(SocketPusher.class);

    static {
        // 只装配这个log，其他的log实例不变
        ch.qos.logback.classic.Logger ins = (ch.qos.logback.classic.Logger)slog;

        // 获取context
        LoggerContext lc = ins.getLoggerContext();


        // 余下代码参考: ch.qos.logback.classic.BasicConfigurator.configure
        SocketLogAppender<ILoggingEvent> sa = new SocketLogAppender<>("MY_OUT");
//        ca.setName("MY_OUT");
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
        encoder.setContext(lc);


        // same as
        // PatternLayout layout = new PatternLayout();
        // layout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        MyTTLLLayout layout = new MyTTLLLayout();

        layout.setContext(lc);
        layout.start();
        encoder.setLayout(layout);

        sa.setContext(lc);
        sa.setEncoder(encoder);
        sa.start();
        ins.addAppender(sa);
    }

    /**
     * getLog。 限制一下获取log的入口
     * @Date 17:02 2021/10/28
     * @Author 温昌营
     **/
    public static Logger getLog(){
        return slog;
    }


    /**
     * 移除
     * @Date 16:46 2021/10/28
     * @Author 温昌营
     **/
    public static void removeByTraceId(String traceId){
        eventMap.remove(traceId);
    }

    /**
     * 获取当前traceId的消息队列
     * @Date 16:46 2021/10/28
     * @Author 温昌营
     **/
    public static BlockingDeque<String> getByTraceId(String traceId){
        BlockingDeque<String> queue = new LinkedBlockingDeque<>();
        BlockingDeque<String> ori;
        if(null == (ori = eventMap.putIfAbsent(traceId, queue))){
            return queue;
        }
        return ori;
    }

    /**
     * 将消息元素放入对应的消息队列。如果队列不存在，创建后放入
     * @Date 16:46 2021/10/28
     * @Author 温昌营
     **/
    public static boolean putEle(String traceId, String ele){
        BlockingDeque<String> byTraceId = getByTraceId(traceId);
        // offer，无法插入则丢弃
        return byTraceId.offer(ele);
    }


}
