package com.tester.testerasync.mq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

public class OnewayProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(2); // 设置自动创建topic的queue数
        producer.start();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("someT_oneway", ("oneway Hello RocketMq" + i).getBytes());
            msg.setKeys("oneway_unique_key_" + i);
            producer.sendOneway(msg);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
