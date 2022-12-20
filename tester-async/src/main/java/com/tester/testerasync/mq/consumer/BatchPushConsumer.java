package com.tester.testerasync.mq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * push类型、集群模式
 */
public class BatchPushConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my_cg");

        // 指定那么server
        consumer.setNamesrvAddr("localhost:9876");
        // 指定从第一条消息开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 指定topic与tag
        consumer.subscribe("Tr_someT", "*");
        // 设置消费模式，默认为 集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 指定每次可以消费10条消息，默认为1
        consumer.setConsumeMessageBatchMaxSize(10);
        // 指定每次可以从broker拉取40条消息，默认为32
        consumer.setPullBatchSize(40);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println(msg + " , content : " + new String(msg.getBody()));
                }
                // 消费成功的返回
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                // 消费异常的返回
                // return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();
        System.out.println("consumer started");
    }
}
