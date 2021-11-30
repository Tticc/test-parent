package com.tester.testercommon.util.dbconnect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单数据库连接工具
 * <br/> 测试用！！！
 * @Author 温昌营
 * @Date 2021-11-25 10:40:07
 */
@Slf4j
public class SimpleDBConnectUtil {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/t_scheduledb?Unicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=GMT%2B8";
        String usr = "root";
        String pwd = "123456";
        String sql = "select * from xxl_job_group limit 5;";
        List<JSONObject> jsonObjects = doQueryList(url, usr, pwd, sql);
        System.out.println("jsonObjects = " + jsonObjects);

    }

    public static List<JSONObject> doQueryList(String url, String usr, String pwd, String sql) throws Exception {
        return doExecute(url,usr,pwd,sql,true);
    }

    public static List<JSONObject> doExecute(String url, String usr, String pwd, String sql, boolean first) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            log.info("开始建立数据库连接。\n {}",url);
            connection = DriverManager.getConnection(url,usr,pwd);
            ps = connection.prepareStatement(sql);
            log.info("开始执行sql：\n {}",sql);
            rs = ps.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();
            int columnCount = rm.getColumnCount();
            List<JSONObject> resList = new ArrayList<>();
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    String name = rm.getColumnName(i).toLowerCase();
                    Object value = rs.getObject(i);
                    jsonObject.put(name,value);
                }
                resList.add(jsonObject);
            }
            log.info("执行完成。 size：{}",resList.size());
            return resList;
        }catch (SQLException se){
            log.error("执行异常.",se);
            if(se.getMessage().contains("No suitable driver found for")){
                // 只重试一次
                if(!first){
                    throw se;
                }
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    Class.forName("com.mysql.jdbc.Driver");
                }
                return doExecute(url,usr,pwd,sql,false);
            }
            throw se;
        }catch (Exception e){
            log.error("执行异常.",e);
            throw e;
        }finally {
            try{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(connection != null){
                    connection.close();
                }
            }catch (Exception e){
                log.error("连接关闭异常.",e);
            }
        }
    }
}
