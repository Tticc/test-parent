package com.tester.testercommon.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author 温昌营
 * @Date 2022-6-16 14:54:18
 */
@Slf4j
public class JdbcDemo {

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
}
