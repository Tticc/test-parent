package com.tester.testermybatis.dao.domain;

import com.tester.testercommon.dao.BaseDomain;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
