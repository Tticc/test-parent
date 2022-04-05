package com.tester.testermybatis.dao.domain;

import com.tester.base.dto.dao.BaseDomain;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 销售单会员信息表
 * @author Liao.
 * @version 1.0, 2019/07/24 
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@DecryptDomain
public class OrderMemberDomain extends BaseDomain {

    public OrderMemberDomain init(){
        super.init();
        return this;
    }
    
    /**
     * 订单号
     */
    private Long orderNo;
    
    /**
     * 会员ID
     */
    private Long memberId;
    
    /**
     * 会员账号
     */
    @DecryptField
    private String memberAccount;

    /**
     * 会员手机号
     */
    @DecryptField
    private String memberPhone;
    
}

/*

-- ----------------------------
        -- Table structure for order_member_0
        -- ----------------------------
        DROP TABLE IF EXISTS `order_member_0`;
        CREATE TABLE `order_member_0`  (
        `id` bigint(20) NOT NULL COMMENT '主键',
        `order_no` bigint(20) NOT NULL COMMENT '订单号',
        `member_id` bigint(20) NOT NULL COMMENT '会员ID',
        `member_account` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '会员账号',
        `member_phone` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '会员手机号',
        `create_time` datetime(0) NULL DEFAULT NULL  COMMENT '创建时间',
        `update_time` datetime(0) NULL DEFAULT NULL  COMMENT '更新时间',
        `revision` int(11) NULL DEFAULT NULL COMMENT '版本号',
        `deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除 0=未删除，1=已删除',
        PRIMARY KEY (`id`) USING BTREE,
        INDEX `idx_order_no`(`order_no`) USING BTREE
        ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '销售单会员信息表 ' ROW_FORMAT = Dynamic;

*/
