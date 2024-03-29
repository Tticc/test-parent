package com.tester.testerasync.mq.producer.msgType;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * 顺序消息
 */
public class OrderedProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("sp");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数
        producer.start();
        for (int i = 0; i < 5; i++) {
            // 选择key orderId，例如实际的orderNo,payNo
            Integer orderId = i;
            Message msg = new Message("someT", ("sync Hello RocketMq" + i).getBytes());
            msg.setKeys(orderId.toString());
            // send方法的第三个参数orderId，将会传递给内部类select方法的第三个参数
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                // 第一个参数的mqs是集群所有broker的所有queue吗？
                // 如果顺序消息发送期间恰好有个broker下线了，且producer感知到了，那么mqs数量是不是发生了变化？
                // 变化之后取模不一样了，发到了不同的queue？
                // 是的，发到其他queue，因此会乱序
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer)arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            },orderId);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}
