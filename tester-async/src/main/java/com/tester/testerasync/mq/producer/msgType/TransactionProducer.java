package com.tester.testerasync.mq.producer.msgType;

import com.tester.testercommon.util.pool.MyThreadFactoryImpl;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 事务消息
 */
public class TransactionProducer {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("sp2c");
        producer.setNamesrvAddr("localhost:9876");
        producer.setDefaultTopicQueueNums(4); // 设置自动创建topic的queue数

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), new MyThreadFactoryImpl("transaction-test"));
        // 为生产者指定一个线程池
        producer.setExecutorService(executorService);
        // 为生产者指定事务监听器
        producer.setTransactionListener(new ICBCTransactionListener());
        producer.start();
        String[] tags = new String[]{"tagA","tagB","tagC"};
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("Tr_someT",tags[i%3], ("transaction Hello RocketMq" + i).getBytes());
            msg.setKeys("sync_unique_key_" + i);
            // 第二个参数是用于执行本地事务时用到的业务参数
            SendResult sendResult = producer.sendMessageInTransaction(msg,null);
            System.out.println(sendResult);
            TimeUnit.MICROSECONDS.sleep(1000);
        }

        TimeUnit.SECONDS.sleep(330);
        producer.shutdown();
        System.out.println("producer shutdown");
    }

    public static class ICBCTransactionListener implements TransactionListener {
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("消息预提交成功："+msg);
            if("tagA".equals(msg.getTags())){
                // 表示扣款成功
                return LocalTransactionState.COMMIT_MESSAGE;
            }else if("tagB".equals(msg.getTags())){
                // 表示扣款失败
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }else if("tagC".equals(msg.getTags())){
                // 表示不清楚，需要回查
                System.out.println("tagC 发送时间点："+new Date());
                return LocalTransactionState.UNKNOW;
            }
            return LocalTransactionState.UNKNOW;
        }
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            System.out.println("执行回查消息。msgId:"+msg);
            System.out.println("tagC 回查时间点："+new Date());
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}
