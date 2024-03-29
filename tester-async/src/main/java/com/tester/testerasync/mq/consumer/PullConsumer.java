package com.tester.testerasync.mq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DefaultLitePullConsumer需要的版本高于4.5.1，暂时废弃
 */
public class PullConsumer {
    // 记录每个队列的消费进度
    private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();

    public static void main(String[] args) throws Exception {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("my_cg");
        // 指定那么server
        consumer.setNamesrvAddr("localhost:9876");
        // 设置消费模式，默认为 集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.start();
        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("topic");

        for (MessageQueue mq : mqs) {
            System.out.printf("Consume from the queue: %s%n", mq);
            SINGLE_MQ:
            while (true) {
                try {
                    // 5. 拉取消息，arg1=消息队列，arg2=tag消息过滤，arg3=消息队列，arg4=一次最大拉去消息数量
                    PullResult pullResult =
                            consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.printf("%s%n", pullResult);
                    // 6. 将消息放入hash表中，存储该队列的消费进度
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()) {
                        case FOUND:  // 找到消息，输出
                            System.out.println(pullResult.getMsgFoundList().get(0));
                            break;
                        case NO_MATCHED_MSG:  // 没有匹配tag的消息
                            System.out.println("无匹配消息");
                            break;
                        case NO_NEW_MSG:  // 该队列没有新消息，消费offset=最大offset
                            System.out.println("没有新消息");
                            break SINGLE_MQ;  // 跳出该队列遍历
                        case OFFSET_ILLEGAL:  // offset不合法
                            System.out.println("Offset不合法");
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 7. 关闭Consumer
        consumer.shutdown();
        System.out.println("consumer started");
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    /**
     * 将消费进度更新到Hash表
     *
     * @param mq     消息队列
     * @param offset offset
     */
    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }
}
