package com.tester.testerasync.mq.producer.msgOpe;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO 待完成
 */
public class BatchProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数
        producer.start();
        List<Message> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("batch_someT", ("batch Hello RocketMq" + i).getBytes());
            msg.setKeys("batch_unique_key_" + i);
            list.add(msg);
        }
        SendResult sendResult = producer.send(list);
        System.out.println(sendResult);
        TimeUnit.MICROSECONDS.sleep(1000);
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
