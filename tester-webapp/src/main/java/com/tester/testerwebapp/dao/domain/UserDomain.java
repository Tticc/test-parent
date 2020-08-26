package com.tester.testerwebapp.dao.domain;

import com.tester.testercommon.dao.BaseDomain;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
@Table(name = "u_person")
public class UserDomain extends BaseDomain {
    public UserDomain init(){
        super.init();
        return this;
    }
    /** 员工号;员工号 */
    private String employeeId ;

    /***************************** ext包含的数据 **********************/
    /** ext_person_id;外部人员编号 */
    private String extPersonId ;
    /** 名字 */
    private String name ;
    /** 手机号 */
    private String cellphone ;
    /** 性别 1=男，0=女*/
    private Integer gender ;
    /** 邮箱 */
    private String email ;
    /** 英文名 */
    private String enname ;
    /** 状态 */
    private Integer status ;
    /***************************** ext包含的数据 **********************/


    /** 微信ID */
    private String wechatid ;
    /** 备注 */
    private String note ;
    /** 数据来源;PlatformEnum */
    private Integer dataFrom ;
}
/**
 -- ----------------------------
 -- Table structure for u_person
 -- ----------------------------
 DROP TABLE IF EXISTS `u_person`;
 CREATE TABLE `u_person`  (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
 `name` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '名字',
 `ext_person_id` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'ext_person_id',
 `employee_id` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '员工号 员工号',
 `cellphone` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '手机号',
 `gender` int(11) NULL DEFAULT NULL COMMENT '性别 1=男，0=女',
 `email` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '邮箱',
 `enname` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '英文名',
 `wechatid` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '微信ID',
 `status` int(11) NULL DEFAULT NULL COMMENT '状态',
 `note` varchar(512) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注',
 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
 `revision` int(11) NULL DEFAULT NULL COMMENT '版本号',
 `deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除 0=未删除，1=已删除',
 `data_from` int(11) NULL DEFAULT NULL COMMENT '数据来源 1=SAP，2=cloudoffice，默认1',
 PRIMARY KEY (`id`) USING BTREE
 ) ENGINE = InnoDB AUTO_INCREMENT = 3188 CHARACTER SET = utf8mb4 COMMENT = '人员表 人员表' ROW_FORMAT = Dynamic;
 **/