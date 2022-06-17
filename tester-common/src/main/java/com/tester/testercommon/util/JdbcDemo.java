package com.tester.testercommon.util;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * jdbc 和 spring-boot-starter-data-jdbc案例
 * @Author 温昌营
 * @Date 2022-6-16 14:54:18
 */
@Slf4j
public class JdbcDemo {

    /**
     * 1. jdbc 案例
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

    // 2. spring-boot-starter-data-jdbc 案例
    //  <dependency>
    //      <groupId>org.springframework.boot</groupId>
    //      <artifactId>spring-boot-starter-data-jdbc</artifactId>
    //  </dependency>
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

    // 3. jpa案例
    //  <dependency>
    //      <groupId>org.springframework.boot</groupId>
    //      <artifactId>spring-boot-starter-data-jpa</artifactId>
    //  </dependency>

    // service
//    @Autowired
//    private PersonRepository personRepository;
//    UserDomain userDomain = personRepository.findById((long) 1).orElseGet(null);
//    System.out.println(userDomain);

    // repository(DAO)
//    public interface PersonRepository extends CrudRepository<UserDomain, Long> {
//        List<UserDomain> findByName(String name);
//    }

    // entity(Domain)
    //import lombok.Data;
    //import lombok.experimental.Accessors;
    //
    //import javax.persistence.Entity;
    //import javax.persistence.GeneratedValue;
    //import javax.persistence.Id;
    //import javax.persistence.Table;
    //import java.util.Date;
    //
    //    /**
    //     * @Author 温昌营
    //     * @Date
    //     */
    //    @Entity
    //    @Table(name = "u_person") // 如果不指定table名，那么默认的table名将会是：user_domain
    //    @Data
    //    @Accessors(chain = true)
    //    public class UserDomain {
    //
    //        // @Id和@GeneratedValue是重要的注解
    //        @Id
    //        @GeneratedValue
    //        private Long id;
    //        private Date createTime;
    //        private Date updateTime;
    //
    //        private Integer deleted;
    //        private Integer revision;
    //
    //        /**
    //         * 员工号;员工号
    //         */
    //        private String employeeId;
    //
    //        /***************************** ext包含的数据 **********************/
    //        /**
    //         * ext_person_id;外部人员编号
    //         */
    //        private String extPersonId;
    //        /**
    //         * 名字
    //         */
    //        private String name;
    //        /**
    //         * 手机号
    //         */
    //        private String cellphone;
    //        /**
    //         * 性别 1=男，0=女
    //         */
    //        private Integer gender;
    //        /**
    //         * 邮箱
    //         */
    //        private String email;
    //        /**
    //         * 英文名
    //         */
    //        private String enname;
    //        /**
    //         * 状态
    //         */
    //        private Integer status;
    //        /***************************** ext包含的数据 **********************/
    //
    //
    //        /**
    //         * 微信ID
    //         */
    //        private String wechatid;
    //        /**
    //         * 备注
    //         */
    //        private String note;
    //        /**
    //         * 数据来源;PlatformEnum
    //         */
    //        private Integer dataFrom;
    //    }
    ///**
    // * -- ----------------------------
    // * -- Table structure for u_person
    // * -- ----------------------------
    // * DROP TABLE IF EXISTS `u_person`;
    // * CREATE TABLE `u_person`  (
    // * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    // * `name` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '名字',
    // * `ext_person_id` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'ext_person_id',
    // * `employee_id` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '员工号 员工号',
    // * `cellphone` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '手机号',
    // * `gender` int(11) NULL DEFAULT NULL COMMENT '性别 1=男，0=女',
    // * `email` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '邮箱',
    // * `enname` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '英文名',
    // * `wechatid` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '微信ID',
    // * `status` int(11) NULL DEFAULT NULL COMMENT '状态',
    // * `note` varchar(512) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注',
    // * `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    // * `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    // * `revision` int(11) NULL DEFAULT NULL COMMENT '版本号',
    // * `deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除 0=未删除，1=已删除',
    // * `data_from` int(11) NULL DEFAULT NULL COMMENT '数据来源 1=SAP，2=cloudoffice，默认1',
    // * PRIMARY KEY (`id`) USING BTREE
    // * ) ENGINE = InnoDB AUTO_INCREMENT = 3188 CHARACTER SET = utf8mb4 COMMENT = '人员表 人员表' ROW_FORMAT = Dynamic;
    // **/
}
