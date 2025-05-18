package com.tester.testersearch.config.sharding.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用于非分库分表的普通数据源属性。
 *
 * @Author 温昌营
 * @Date 2020-8-20 18:23:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "sharding-datasource")
public class ShardingDatabaseProperties {
    /**
     * 逻辑表
     */
    private String logicTable;
    /**
     * 仅需分库的逻辑表
     */
    private String logicTableOnlySplitDb;
    /**
     * 分库字段
     */
    private String shardingDbColumn;
    /**
     * 分表字段
     */
    private String shardingTableColumn;
    /**
     * 分库链接配置
     */
    Map<String, DbProperties> datasources;
    /**
     * 是否打印真实sql
     */
    private boolean showSql = true;




    // 其他配置
    private Integer maximumPoolSize = 60;

    private Integer minimumIdle = 2;

    private Integer maxLifetime = 60000;

    private long connectionTimeout = 60000;

    private boolean cachePrepStmts = false;

    private boolean useServerPrepStmts = false;

    private int prepStmtCacheSize = 250;

    private int prepStmtCacheSqlLimit = 2048;


    @Data
    @Accessors(chain = true)
    public static class DbProperties{
        /**
         * 驱动类
         */
        private String driverClassName;

        /**
         * 数据库jdbc连接
         * 当只配置数据库服务器时，该属性不包含db实例名 jdbc:mysql://IP:PORT/
         * 当配置数据库实例时，该属性包含db实例名 jdbc:mysql://IP:PORT/DB_NAME
         */
        private String jdbcUrl;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;
        /**
         * 节点
         */
        private String actualDataNodes;
    }

}
