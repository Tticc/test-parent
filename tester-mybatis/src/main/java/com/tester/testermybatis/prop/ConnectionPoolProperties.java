package com.tester.testermybatis.prop;

import lombok.Data;

/**
 * @Author 温昌营
 * @Date 2020-8-20 18:22:45
 */
@Data
public class ConnectionPoolProperties {

    private Integer maximumPoolSize = 60;

    private Integer minimumIdle = 2;

    private Integer maxLifetime = 60000;

    private long connectionTimeout = 60000;

    private boolean cachePrepStmts = false;

    private boolean useServerPrepStmts = false;

    private int prepStmtCacheSize = 250;

    private int prepStmtCacheSqlLimit = 2048;

}
