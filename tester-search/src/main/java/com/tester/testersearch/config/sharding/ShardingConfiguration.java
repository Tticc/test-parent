package com.tester.testersearch.config.sharding;

import com.tester.testersearch.config.sharding.properties.ShardingDatabaseProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.HintShardingStrategyConfiguration;
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
import java.util.stream.Collectors;

@Configuration
public class ShardingConfiguration {

    @Bean
    public DataSource shardingDataSource(ShardingDatabaseProperties databaseProperties) throws SQLException {

        Map<String, DataSource> dataSourceMap = createDataSource(databaseProperties);

        String actualDataNodes = dataSourceMap.keySet().stream()
                .map(ds -> ds + "." + databaseProperties.getDatasources().get(ds).getActualDataNodes())
                .collect(Collectors.joining(","));

        // 配置分表规则
        TableRuleConfiguration tradeDataTableRule = new TableRuleConfiguration(databaseProperties.getLogicTable(), actualDataNodes);

        // 分片策略（按月份）
        tradeDataTableRule.setTableShardingStrategyConfig(
                new StandardShardingStrategyConfiguration(databaseProperties.getShardingTableColumn(), new MonthShardingAlgorithm(),
                        new MonthShardingAlgorithm())
        );
        // 无分库配置
        // tradeDataTableRule.setDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        // 有分库配置
//        tradeDataTableRule.setDatabaseShardingStrategyConfig(
//                new StandardShardingStrategyConfiguration(databaseProperties.getShardingDbColumn(), new DatabaseShardingAlgorithm())
//        );
        tradeDataTableRule.setDatabaseShardingStrategyConfig(
                new HintShardingStrategyConfiguration(new DatabaseHintShardingAlgorithm())
        );


        // 构建分片配置
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(tradeDataTableRule);
        setOnlySplitDbConfig(databaseProperties, dataSourceMap, shardingRuleConfig);

        Properties props = new Properties();
        props.put("sql.show", databaseProperties.isShowSql());
        // 创建 Sharding 数据源
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, props);
    }

    private void setOnlySplitDbConfig(ShardingDatabaseProperties databaseProperties, Map<String, DataSource> dataSourceMap, ShardingRuleConfiguration shardingRuleConfig){
        // 配置 trade_candle_data 表（只分库，不分表）
        String[] split = databaseProperties.getLogicTableOnlySplitDb().split("\\s*,\\s*");
        for (String logicTb : split) {
            TableRuleConfiguration candleDataRule = new TableRuleConfiguration(logicTb,
                    dataSourceMap.keySet().stream()
                            .map(ds -> ds + "."+logicTb)
                            .collect(Collectors.joining(","))
            );
            candleDataRule.setDatabaseShardingStrategyConfig(
                    new StandardShardingStrategyConfiguration(databaseProperties.getShardingDbColumn(), new DatabaseShardingAlgorithm())
            );
            shardingRuleConfig.getTableRuleConfigs().add(candleDataRule);
        }
    }

    private Map<String, DataSource> createDataSource(ShardingDatabaseProperties databaseProperties) {
        Map<String, ShardingDatabaseProperties.DbProperties> dataSources = databaseProperties.getDatasources();
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for (Map.Entry<String, ShardingDatabaseProperties.DbProperties> ds : dataSources.entrySet()) {
            String key = ds.getKey();
            ShardingDatabaseProperties.DbProperties dbProperties = ds.getValue();
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setPoolName("dsPool_" + key);
            dataSource.setDriverClassName(dbProperties.getDriverClassName());
            dataSource.setJdbcUrl(dbProperties.getJdbcUrl());
            dataSource.setUsername(dbProperties.getUsername());
            dataSource.setPassword(dbProperties.getPassword());
            dataSource.setMaximumPoolSize(databaseProperties.getMaximumPoolSize());
            dataSource.setConnectionTimeout(databaseProperties.getConnectionTimeout());
            dataSource.setMinimumIdle(databaseProperties.getMinimumIdle());
            dataSource.setMaxLifetime(databaseProperties.getMaxLifetime());
            dataSource.addDataSourceProperty("cachePrepStmts", databaseProperties.isCachePrepStmts());
            dataSource.addDataSourceProperty("prepStmtCacheSize", databaseProperties.getPrepStmtCacheSize());
            dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", databaseProperties.getPrepStmtCacheSqlLimit());
            dataSource.addDataSourceProperty("useServerPrepStmts", databaseProperties.isUseServerPrepStmts());
            dataSourceMap.put(key, dataSource);
        }
        return dataSourceMap;
    }


    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
