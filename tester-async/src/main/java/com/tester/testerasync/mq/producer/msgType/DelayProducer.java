package com.tester.testerasync.mq.producer.msgType;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;
/**
 * 延时消息
 */
public class DelayProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数
        producer.start();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("someT", ("sync Hello RocketMq" + i).getBytes());
            msg.setKeys("sync_unique_key_" + i);
            // 延迟10s
            msg.setDelayTimeLevel(3);
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
