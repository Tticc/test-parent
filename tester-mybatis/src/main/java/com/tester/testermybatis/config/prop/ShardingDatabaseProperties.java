package com.tester.testermybatis.config.prop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于shardingjdbc分库分表用的属性集合。与NormalDatabaseProperties同等级别。
 * @Author 温昌营
 * @Date 2020-8-20 18:23:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardingDatabaseProperties extends ConnectionPoolProperties{
    /**
     * 驱动类
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库jdbc连接
     * 当只配置数据库服务器时，该属性不包含db实例名 jdbc:mysql://IP:PORT/
     * 当配置数据库实例时，该属性包含db实例名 jdbc:mysql://IP:PORT/DB_NAME
     */
    private String jdbcUrl = "jdbc:mysql://localhost:3306/";

    /**
     * jdbc连接参数，例：?useSSL=false&useUnicode=true&characterEncoding=utf-8
     */
    private String jdbcUrlParam = "?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8";

    /**
     * 用户名
     */
    private String username = "root";

    /**
     * 密码
     */
    private String password = "123456";

    /**
     * db实例前缀
     * 当只配置数据库服务器时，需配置该值及服务器上db实例索引范围
     */
    private String dbPrefix = "mydb_";

    /**
     * 服务器db实例索引范围：起
     */
    private Integer firstDbIndex = 0;

    /**
     * 服务器db实例索引范围：止
     */
    private Integer lastDbIndex = 2;
}
