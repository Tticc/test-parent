package com.tester.thirdparty.rocketmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 温昌营
 * @Date 2021-11-2 13:47:42
 */
@Data
@Configuration
public class RocketMQConfigurer {

    @Value("${data.tools.auto.release.rocketmq.nameservaddr:xx.xx.xx.xx:9876}")
    private String mqNameServAddr;

    @Value("${data.tools.auto.release.rocketmq.broker.default:broker-a}")
    private String mqBroker;

}
