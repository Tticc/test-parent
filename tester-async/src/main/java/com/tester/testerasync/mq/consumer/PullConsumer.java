//package com.tester.testerasync.mq.consumer;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
//
//import java.util.List;
//
///**
// * DefaultLitePullConsumer需要的版本高于4.5.1，暂时废弃
// */
//public class PullConsumer {
//    public static void main(String[] args) throws Exception {
//        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("my_cg");
//
//        // 指定那么server
//        consumer.setNamesrvAddr("localhost:9876");
//        // 指定从第一条消息开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        // 指定topic与tag
//        consumer.subscribe("someT", "*");
//        // 设置消费模式，默认为 集群模式
//        consumer.setMessageModel(MessageModel.CLUSTERING);
//
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                for (MessageExt msg:msgs){
//                    System.out.println(msg+ " , content : "+ new String(msg.getBody()));
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//        consumer.start();
//        System.out.println("consumer started");
//    }
//}
