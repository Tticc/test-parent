package com.tester.testermybatis.config.prop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 用于非分库分表的普通数据源属性。
 * @Author 温昌营
 * @Date 2020-8-20 18:23:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "normal-datasource")
public class NormalDatabaseProperties extends ConnectionPoolProperties{
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


}
