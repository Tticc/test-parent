package org.springframework.boot.autoconfigure.data.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.time.Duration;

/**
 * 配置redis连接。
 * <p/>
 * <ol>
 *     <li>1.继承了LettuceConnectionConfiguration。且内容基本上与LettuceConnectionConfiguration类似。除了在getLettuceClientConfiguration里增加了定时拉取redis集群的数据</li>
 *     <li>2.本类的redisConnectionFactory的@Bean方法默认是不会被执行并实例化的，spring会实例化父类的。所以加了配置在resource/MATA-INF/spring.factories。
 *          <br/>配置内容为：
 *          <p>org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 *          org.springframework.boot.autoconfigure.data.redis.MyLettuceConnectionConfiguration</p>
 *     </li>
 *     <li>3.对于第2点，其实还有另一种解决方法，就是新建一个config，在config上import。
 *          <br/>如：
 *              <br/>com.tester.testerwebapp.config.LettuceConnectionConfig所示。
 *          <br/> 注：实际上更推荐2。因为可以把这个配置抽离为一个jar包，需要使用的地方直接引用即可，不需要做额外的配置
 *     </li>
 * </ol>
 * @Date 16:41 2020/11/11
 * @Author 温昌营
 **/
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyLettuceConnectionConfiguration extends LettuceConnectionConfiguration {


    private final RedisProperties properties;
    private final ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;
    @Value("${spring.redis.cluster.topology-refresh-period:5}")
    private long topologyRefreshPeriod = 600L;

    MyLettuceConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider, ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider, ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers) {
        super(properties, sentinelConfigurationProvider, clusterConfigurationProvider, builderCustomizers);
        this.properties = properties;
        this.builderCustomizers = builderCustomizers;

    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) throws UnknownHostException {
        LettuceClientConfiguration clientConfig = this.getLettuceClientConfiguration(clientResources, this.properties.getLettuce().getPool());
        LettuceConnectionFactory lettuceConnectionFactory = this.createLettuceConnectionFactory(clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        lettuceConnectionFactory.setShareNativeConnection(false);
        return lettuceConnectionFactory;
    }

    /**
     * lettuce读写分离配置<br/>
     * 当前为优先读取slave，slave不可用则读取master
     *
     * @return
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer configurationBuilderCustomizer(){
        return configBuilder -> configBuilder.readFrom(ReadFrom.SLAVE_PREFERRED);
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
        if (this.getSentinelConfig() != null) {
            return new LettuceConnectionFactory(this.getSentinelConfig(), clientConfiguration);
        } else {
            return this.getClusterConfiguration() != null ? new LettuceConnectionFactory(this.getClusterConfiguration(), clientConfiguration) : new LettuceConnectionFactory(this.getStandaloneConfig(), clientConfiguration);
        }
    }

    private LettuceClientConfiguration getLettuceClientConfiguration1(ClientResources clientResources, Pool pool) {
        LettuceClientConfigurationBuilder builder = createBuilder(pool);
        applyProperties(builder);
        if (StringUtils.hasText(this.properties.getUrl())) {
            customizeConfigurationFromUrl(builder);
        }
        builder.clientResources(clientResources);
        customize(builder);
        return builder.build();
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources, Pool pool) {
        LettuceClientConfigurationBuilder builder = this.createBuilder(pool);
        this.applyProperties(builder);
        if (StringUtils.hasText(this.properties.getUrl())) {
            this.customizeConfigurationFromUrl(builder);
        }

        builder.clientResources(clientResources);
        this.customize(builder);

        // 这里与父类方法不一致，增加了定时拉取redis集群数据的逻辑
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh(Duration.ofSeconds(this.topologyRefreshPeriod)).build();
        ClientOptions clientOptions = ClusterClientOptions.builder().timeoutOptions(TimeoutOptions.enabled()).autoReconnect(true).topologyRefreshOptions(clusterTopologyRefreshOptions).build();
        builder.clientOptions(clientOptions);
        // 这里与父类方法不一致，增加了定时拉取redis集群数据的逻辑


        return builder.build();
    }

    private LettuceClientConfigurationBuilder applyProperties(LettuceClientConfigurationBuilder builder) {
        if (this.properties.isSsl()) {
            builder.useSsl();
        }

        if (this.properties.getTimeout() != null) {
            builder.commandTimeout(this.properties.getTimeout());
        }

        if (this.properties.getLettuce() != null) {
            Lettuce lettuce = this.properties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(this.properties.getLettuce().getShutdownTimeout());
            }
        }

        return builder;
    }

    private void customizeConfigurationFromUrl(LettuceClientConfigurationBuilder builder) {
        ConnectionInfo connectionInfo = this.parseUrl(this.properties.getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }

    }

    private LettuceClientConfigurationBuilder createBuilder(Pool pool) {
        return pool == null ? LettuceClientConfiguration.builder() : (new MyLettuceConnectionConfiguration.PoolBuilderFactory()).createBuilder(pool);
    }

    private void customize(LettuceClientConfigurationBuilder builder) {
        this.builderCustomizers.orderedStream().forEach((customizer) -> {
            customizer.customize(builder);
        });
    }

    private static class PoolBuilderFactory {
        private PoolBuilderFactory() {
        }

        public LettuceClientConfigurationBuilder createBuilder(Pool properties) {
            return LettucePoolingClientConfiguration.builder().poolConfig(this.getPoolConfig(properties));
        }

        private GenericObjectPoolConfig<?> getPoolConfig(Pool properties) {
            GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig();
            config.setMaxTotal(properties.getMaxActive());
            config.setMaxIdle(properties.getMaxIdle());
            config.setMinIdle(properties.getMinIdle());
            if (properties.getTimeBetweenEvictionRuns() != null) {
                config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
            }

            if (properties.getMaxWait() != null) {
                config.setMaxWaitMillis(properties.getMaxWait().toMillis());
            }

            return config;
        }
    }
}
