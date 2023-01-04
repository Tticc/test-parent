package com.tester.testerasync.mq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

public class SyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        // 设置自动创建topic的queue数
        producer.setDefaultTopicQueueNums(4);
        // 设置同步发送最大重试次数。默认为2
        producer.setRetryTimesWhenSendFailed(2);
        // 设置异步发送最大重试次数。默认为2
        producer.setRetryTimesWhenSendAsyncFailed(2);
        // 发送超时时间，默认为3000ms
        producer.setSendMsgTimeout(3000);
        producer.start();
        for (int i = 0; i < 5; i++) {
            Message msg = new Message("someT", ("sync Hello RocketMq" + i).getBytes());
            msg.setKeys("sync_unique_key_" + i);
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
