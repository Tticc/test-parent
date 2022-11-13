package com.tester.testerwebapp.config;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 * <br/>
 *
 * @Author 温昌营
 * @Date 2022-11-11 16:28:44
 */

@Configuration
public class RedissonConfigurer {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.addNodeAddress("", "", "");
        return Redisson.create(config);
    }
    @Bean
    public void redissonClient1() {
        RedissonClient redissonClient = redissonClient();
        RLock xx = redissonClient.getLock("xx");
        boolean b = xx.tryLock();
        xx.unlock();

    }

}
