package com.tester.testerasync.mq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

public class AsyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数
        producer.start();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("someT", ("async Hello RocketMq" + i).getBytes());
            msg.setKeys("async_unique_key_" + i);
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("print on success:" + sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        TimeUnit.MICROSECONDS.sleep(5000);
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
