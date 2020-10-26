package com.tester.testerasync.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Consumer {

    public static void main(String[] args) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction_producer");

        consumer.setNamesrvAddr("192.168.1.114:9876;192.168.1.115:9876;192.168.1.116:9876;192.168.1.116:9876");

        /**

         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>

         * 如果非第一次启动，那么按照上次消费的位置继续消费

         */

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("TopicTransaction", "*");

        consumer.registerMessageListener(new MessageListenerOrderly() {

            private Random random = new Random();

            @Override

            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                //设置自动提交

                context.setAutoCommit(true);

                for (MessageExt msg:msgs){

                    System.out.println(msg+ " , content : "+ new String(msg.getBody()));

                }

                try {

                    //模拟业务处理

                    TimeUnit.SECONDS.sleep(random.nextInt(5));

                }catch (Exception e){

                    e.printStackTrace();

                    return  ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;

                }

                return ConsumeOrderlyStatus.SUCCESS;

            }

        });

        consumer.start();

        System.out.println("consumer start ! ");

    }

}