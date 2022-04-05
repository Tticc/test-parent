package com.tester.testermybatis.dao.domain;

import com.tester.base.dto.dao.BaseDomain;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author 温昌营
 * @Date 2020-8-21 14:27:20
 */
@Accessors(chain = true)
@Data
@DecryptDomain
@ToString(callSuper = true)
public class OrderItemDomain extends BaseDomain {
    public OrderItemDomain init(){
        super.init();
        return this;
    }
    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKUID
     */
    private Long skuId;

    /**
     * SKU名称
     */
    @DecryptField
    private String skuName;

    /**
     * 数量
     */
    private BigDecimal saleQty;

    /**
     * 销售单价
     */
    private BigDecimal salePrice;

    /**
     * 商品重量（单）
     */
    private BigDecimal weight;

    /**
     * 销售单位
     */
    private String saleUnit;

    /**
     * 商品行类型 0=普通商品，1=组合商品
     */
//    private Integer itemType;


}


/*

-- ----------------------------
        -- Table structure for order_item_0
        -- ----------------------------
        DROP TABLE IF EXISTS `order_item_0`;
        CREATE TABLE `order_item_0`  (
        `id` bigint(20) NOT NULL COMMENT '主键',
        `member_id` bigint(20) NOT NULL COMMENT '会员ID',
        `order_no` bigint(20) NOT NULL COMMENT '订单号',
        `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
        `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'SKUID',
        `sku_name` varchar(150) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'SKU名称',
        `sale_qty` decimal(7, 2) NULL DEFAULT NULL COMMENT '数量',
        `sale_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售单价',
        `weight` decimal(12, 6) NULL DEFAULT NULL COMMENT '商品重量（单） 单位kg',
        `sale_unit` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售单位',
        `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
        `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
        `revision` int(11) NULL DEFAULT NULL COMMENT '版本号',
        `deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除 0=未删除，1=已删除',
        PRIMARY KEY (`id`) USING BTREE,
        INDEX `idx_order_no`(`order_no`) USING BTREE
        ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '商品明细表 ' ROW_FORMAT = Dynamic;

*/

