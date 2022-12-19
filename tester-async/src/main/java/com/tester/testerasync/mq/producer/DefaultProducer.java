package com.tester.testerasync.mq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

public class DefaultProducer {

    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        String group_name = "transaction_producer";
        final DefaultMQProducer producer = new DefaultMQProducer(group_name);
        //namesev服务
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        for (int i = 0; i < 1; i++) {
            Message msg = new Message("test_1", ("Hello RocketMq" + i).getBytes());
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                producer.shutdown();
            }
        }));
        System.exit(0);
    }

}