package com.tester.testerasync.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.TimeUnit;

public class Producer {

    public static void main(String[] args) throws MQClientException, InterruptedException {

        String group_name = "transaction_producer";

        final TransactionMQProducer producer = new TransactionMQProducer(group_name);

        //namesev服务

        producer.setNamesrvAddr("192.168.1.114:9876;192.168.1.115:9876;192.168.1.116:9876;192.168.1.116:9876");

        //事务回查最小并发数

        producer.setCheckThreadPoolMinSize(5);

        //事务回查最大并发数

        producer.setCheckThreadPoolMaxSize(20);

        //队列数

        producer.setCheckRequestHoldMax(2000);

        producer.start();

        //服务器回调producer,检查本地事务分支成功还是失败

        producer.setTransactionCheckListener(new TransactionCheckListener() {

            @Override

            public LocalTransactionState checkLocalTransactionState(MessageExt messageExt) {

                System.out.println("state --" + new String(messageExt.getBody()));

                return LocalTransactionState.COMMIT_MESSAGE;

            }

        });

        TransactionExecuterImpl transactionExecuter = new TransactionExecuterImpl();

        for (int i = 0; i < 2; i++) {

            Message msg = new Message("TopicTransaction",

                    "Transaction" + i,

                    ("Hello RocketMq" + i).getBytes()

            );

            SendResult sendResult = producer.sendMessageInTransaction(msg, transactionExecuter, "tq");

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