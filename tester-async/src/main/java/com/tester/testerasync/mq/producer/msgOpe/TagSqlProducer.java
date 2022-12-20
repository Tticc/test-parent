package com.tester.testerasync.mq.producer.msgOpe;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 指定tag、SQL过滤
 */
public class TagSqlProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数
        producer.start();
        for (int i = 0; i < 5; i++) {
            // 指定tag
            Message msg = new Message("someT","tagA", ("sync Hello RocketMq" + i).getBytes());
            msg.setKeys("sync_unique_key_" + i);
            // 设置sql过滤需要的 用户属性
            msg.putUserProperty("age",i+"");

            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
