package com.tester.testermybatis.prop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 温昌营
 * @Date 2020-8-20 18:23:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NormalDatabaseProperties extends ConnectionPoolProperties{
    /**
     * 驱动类
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库jdbc连接
     * 当只配置数据库服务器时，该属性不包含db实例名 jdbc:mysql://IP:PORT/
     * 当配置数据库实例时，该属性包含db实例名 jdbc:mysql://IP:PORT/DB_NAME
     */
    private String jdbcUrl = "jdbc:mysql://localhost:3306/test_parent?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8";

    /**
     * 用户名
     */
    private String username = "root";

    /**
     * 密码
     */
    private String password = "123456";


}
