package com.tester.testermybatis.config.prop;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2020-8-20 18:20:54
 */
@ConfigurationProperties(prefix = "sharding-my-db")
@Data
public class MyDatabaseProperties implements InitializingBean {
    /**
     * true 每个实例单独配置
     * false 配置服务器及服务器中db实例的序号端
     */
    private boolean strict = false;

    /**
     * strict=false时，每个db实例分片数量<br/>
     * 注：由于orderNo生成算法限制，不可超过2位数<br/>
     * com.tester.testermybatis.service.MyKeyGenerator#generateOrderNo
     */
    private Integer shardingNumPerDb = 2;

    /**
     * 数据源配置。在 afterPropertiesSet 初始化
     * strict=true 每个数据源配置
     * strict=false 服务器配置
     */
    private Map<String, ShardingDatabaseProperties> datasource = new HashMap<>();

    /**
     * 逻辑表名
     */
    private String logicTables = "order_item,order_member";

    /**
     * 逻辑表对应真实分片（strict=true时配置）
     */
    private Map<String, String> tableActualDataNodes;

    /**
     * 是否打印真实sql
     */
    private boolean showSql = true;

    /**
     * 分库数量。<br/>
     * 注：由于orderNo生成算法限制，不可超过3位数<br/>
     * com.tester.testermybatis.service.MyKeyGenerator#generateOrderNo
     */
    private Integer allDbNum;

    /**
     * 获取所有分库数量
     * <p>
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (allDbNum != null) {
            return;
        }

        if (this.strict) {
            allDbNum = datasource.size();
        } else {
            Integer lastDbIndex = 0;
            for (ShardingDatabaseProperties properties : datasource.values()) {
                if (properties.getLastDbIndex() > lastDbIndex) {
                    lastDbIndex = properties.getLastDbIndex();
                }
            }
            allDbNum = lastDbIndex + 1;
        }

        datasource.put("server1",new ShardingDatabaseProperties(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/",
                "?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8",
                "root",
                "123456",
                "mydb_",
                0,
                // 注：由于orderNo生成算法限制，不可超过3位数
                2
        ));
    }
}
