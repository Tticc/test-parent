package com.tester.testercommon.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * jdbc 和 spring-boot-starter-data-jdbc案例
 * @Author 温昌营
 * @Date 2022-6-16 14:54:18
 */
@Slf4j
public class JdbcDemo {

    /**
     * jdbc
     * @Date 14:57 2022/6/16
     * @Author 温昌营
     **/
    public static void getConnection() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test_parent?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true","root", "123456");
        PreparedStatement pstm =connection.prepareStatement("select * from u_person where `name` = ? order by id limit 2");
        pstm.setObject(1,"wenc");
        ResultSet rs = pstm.executeQuery();
        while(rs.next()) {
            log.info("id:{},name:{},ext_person_id:{},cellphone:{}"
                    ,rs.getString("id")
                    ,rs.getString("name")
                    ,rs.getString("ext_person_id")
                    ,rs.getString("cellphone"));
        }
    }

    // spring-boot-starter-data-jdbc 案例
//
//    // 数据源组件
//    @Autowired
//    DataSource dataSource;
//
//    // 用于访问数据库的组件
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @GetMapping("/getData")
//    public List<UserDomain> getData() {
//        List<UserDomain> query = jdbcTemplate.query("SELECT * FROM `u_person` limit 3", new BeanPropertyRowMapper<>(UserDomain.class));
//        return query;
//    }
}
