package com.tester.testersearch.config.sharding;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingConfiguration {

    @Bean
    public DataSource normalDataSource(NormalDatabaseProperties normalDatabaseProperties) throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("normalDataSourcePool");
        dataSource.setDriverClassName(normalDatabaseProperties.getDriverClassName());
        dataSource.setJdbcUrl(normalDatabaseProperties.getJdbcUrl());
        dataSource.setUsername(normalDatabaseProperties.getUsername());
        dataSource.setPassword(normalDatabaseProperties.getPassword());
        dataSource.setMaximumPoolSize(normalDatabaseProperties.getMaximumPoolSize());
        dataSource.setConnectionTimeout(normalDatabaseProperties.getConnectionTimeout());
        dataSource.setMinimumIdle(normalDatabaseProperties.getMinimumIdle());
        dataSource.setMaxLifetime(normalDatabaseProperties.getMaxLifetime());
        dataSource.addDataSourceProperty("cachePrepStmts", normalDatabaseProperties.isCachePrepStmts());
        dataSource.addDataSourceProperty("prepStmtCacheSize", normalDatabaseProperties.getPrepStmtCacheSize());
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", normalDatabaseProperties.getPrepStmtCacheSqlLimit());
        dataSource.addDataSourceProperty("useServerPrepStmts", normalDatabaseProperties.isUseServerPrepStmts());


        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(normalDatabaseProperties.getDs(), dataSource);

        // 配置分表规则
        TableRuleConfiguration tradeDataTableRule = new TableRuleConfiguration(normalDatabaseProperties.getLogicTable(), normalDatabaseProperties.getDs() + "." + normalDatabaseProperties.getActualDataNodes());

        // 分片策略（按月份）
        tradeDataTableRule.setTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration("id", new MonthShardingAlgorithm(),
                        new MonthShardingAlgorithm())
        );
        tradeDataTableRule.setDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());

        // 构建分片配置
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tradeDataTableRule);

        Properties props = new Properties();
        props.put("sql.show", normalDatabaseProperties.isShowSql());
        // 创建 Sharding 数据源
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, props);
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
