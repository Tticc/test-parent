package com.tester.testerasync.mq.consumer;

import com.tester.testerasync.constants.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;


/**
 * 过期券消息监听器
 *
 * @Date 17:02 2021/7/9
 * @Author 温昌营
 **/

@Service
@RocketMQMessageListener(topic = MqConstant.MQ_TOPIC_COUPON_OUTDATE_PROCESS,
        consumerGroup = "${my.rocketmq.default.consumer.group}-" + MqConstant.TEST_ONE_GROUP1)
@Slf4j
public class OutdateCouponConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String param) {
        log.info("过期券消息：{}", param);
        // 抛异常，触发mq重试
        throw new RuntimeException(new Exception());

    }


}
