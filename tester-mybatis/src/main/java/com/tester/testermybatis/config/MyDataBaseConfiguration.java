package com.tester.testermybatis.config;

import com.tester.testermybatis.prop.ComplexDatabaseShardingAlgorithm;
import com.tester.testermybatis.prop.ComplexTableShardingAlgorithm;
import com.tester.testermybatis.prop.MyDatabaseProperties;
import com.tester.testermybatis.prop.ShardingDatabaseProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author 温昌营
 * @Date 2020-8-20 18:18:22
 */
@Configuration
@EnableConfigurationProperties({MyDatabaseProperties.class,ServerProperties.class})
@MapperScan(basePackages = {"com.tester.testermybatis.dao.mapper"}, sqlSessionFactoryRef = "mySqlSessionFactory")
@Data
@Slf4j
public class MyDataBaseConfiguration {

    private static String virtualDbNamePrefix = "sharding_db_";

    public static final String MY_MANAGER = "transactionManger-my";

    @Autowired
    private MyDatabaseProperties myDatabaseProperties;

    @Autowired
    private ServerProperties serverProperties;

    private Map<String, DataSource> dataSourceMap;

    private String getWorkId(){
        int port = serverProperties.getPort();
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            log.warn("unknown host error");
        }
        int addressHash = (host+":"+port).hashCode();
        log.info("host={}，port={}，host:port hash={}", host, port, addressHash);
        return String.valueOf(Math.abs(addressHash) % 1024);
    }

    @Bean("mySqlSessionFactory")
    public SqlSessionFactory mySqlSessionFactory() throws Exception {
        log.info("====== mySqlSessionFactory init ======");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myDataSource());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:mybatis-config.xml");
        if(resources.length>0){
            sqlSessionFactoryBean.setConfigLocation(resources[0]);
        }
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("myDataSource")
    public DataSource myDataSource() throws SQLException {
        dataSourceMap = createDataSourceMap();
        // 分片规则配置
        ShardingRuleConfiguration rule = createShardRuleConfiguration();
        Properties props = new Properties();
        props.put("sql.show", myDatabaseProperties.isShowSql());
        return ShardingDataSourceFactory.createDataSource(dataSourceMap,rule,props);
    }

    @Bean(MY_MANAGER)
    public DataSourceTransactionManager getDataSourceTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(myDataSource());
    }

    private ShardingRuleConfiguration createShardRuleConfiguration() {
        StringBuilder bindingTableGroup = new StringBuilder();
        List<TableRuleConfiguration> tableRules = new ArrayList<>();
        // 主键生成设置
        Properties props = new Properties();
        props.setProperty("worker.id",getWorkId());
        String maxVibrationOffset = String.valueOf(myDatabaseProperties.getAllDbNum()*myDatabaseProperties.getShardingNumPerDb()-1);
        props.setProperty("max.vibration.offset",maxVibrationOffset);
        // 雪花算法生成id，长整形
        KeyGeneratorConfiguration generatorConfiguration =new KeyGeneratorConfiguration("SNOWFLAKE","id",props);
        // 需要分库的表
        String[] tableNames = myDatabaseProperties.getLogicTables().split(",");
        for (String tableName : tableNames) {
            bindingTableGroup.append(",").append(tableName);
            // 表规则配置
            TableRuleConfiguration tableRule;
            if(this.myDatabaseProperties.isStrict()){
                tableRule = new TableRuleConfiguration(tableName,myDatabaseProperties.getTableActualDataNodes().get(tableName));
            }else {
                // 真实分片表根据分片数量配置
                StringBuilder actualDataNodes = new StringBuilder();
                int index = 0;
                for(Map.Entry<String,DataSource> entry : dataSourceMap.entrySet()){
                    for(int i = 0; i < myDatabaseProperties.getShardingNumPerDb(); i++){
                        actualDataNodes.append(",").append(entry.getKey()).append(".").append(tableName).append("_").append(index++);
                    }
                }
                tableRule = new TableRuleConfiguration(tableName,actualDataNodes.substring(1));
            }
            tableRule.setKeyGeneratorConfig(generatorConfiguration);
            tableRules.add(tableRule);
        }
        ShardingRuleConfiguration shardRuleConfig = new ShardingRuleConfiguration();
        // 表分库分表规则
        shardRuleConfig.setTableRuleConfigs(tableRules);
        // 绑定表
        shardRuleConfig.getBindingTableGroups().add(bindingTableGroup.substring(1));
        // 默认分库策略
        shardRuleConfig.setDefaultDatabaseShardingStrategyConfig(databaseComplexShardStrategy());

        return shardRuleConfig;
    }

    private ComplexShardingStrategyConfiguration databaseComplexShardStrategy() {
        return new ComplexShardingStrategyConfiguration("member_id,order_no", new ComplexDatabaseShardingAlgorithm());
    }
    private ComplexShardingStrategyConfiguration tableComplexShardStrategy() {
        return new ComplexShardingStrategyConfiguration("member_id,order_no", new ComplexTableShardingAlgorithm());
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(myDatabaseProperties.getDatasource().size());
        ShardingDatabaseProperties properties;
        int index = 0;
        Set<Map.Entry<String, ShardingDatabaseProperties>> entries = myDatabaseProperties.getDatasource().entrySet();
        for (Map.Entry<String, ShardingDatabaseProperties> entry : entries) {
            properties = entry.getValue();
            // 每个实例都单独配置
            if(myDatabaseProperties.isStrict()){
                HikariDataSource dataSource = buildDataSource(properties,index++,true);
                result.put(entry.getKey(),dataSource);
            }else {
                Integer first = properties.getFirstDbIndex();
                Integer last = properties.getLastDbIndex();
                for(int i = first; i<=last;i++){
                    HikariDataSource dataSource = buildDataSource(properties,i,false);
                    result.put(virtualDbNamePrefix+(i<10?"0"+i:i),dataSource);
                }
            }
        }
        return result;
    }

    private HikariDataSource buildDataSource(ShardingDatabaseProperties properties, int index, boolean strict) {
        HikariDataSource dataSource = new HikariDataSource();
        if(strict){
            dataSource.setJdbcUrl(properties.getJdbcUrl());
        }else{
            dataSource.setJdbcUrl(properties.getJdbcUrl() + properties.getDbPrefix() + (index < 10 ? "0"+index : index) + properties.getJdbcUrlParam());
        }
        dataSource.setPoolName("myPool-"+index);
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        dataSource.setConnectionTimeout(properties.getConnectionTimeout());
        dataSource.setMinimumIdle(properties.getMinimumIdle());
        dataSource.setMaxLifetime(properties.getMaxLifetime());
        dataSource.addDataSourceProperty("cachePrepStmts",properties.isCachePrepStmts());
        dataSource.addDataSourceProperty("prepStmtCacheSize",properties.getPrepStmtCacheSize());
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit",properties.getPrepStmtCacheSqlLimit());
        dataSource.addDataSourceProperty("useServerPrepStmts",properties.isUseServerPrepStmts());
        return dataSource;
    }
}
