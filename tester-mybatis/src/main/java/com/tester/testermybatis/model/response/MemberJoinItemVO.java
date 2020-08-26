package com.tester.testermybatis.model.response;

import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author 温昌营
 * @Date 2020-8-26 11:22:41
 */
@Data
@Accessors(chain = true)
@DecryptDomain
public class MemberJoinItemVO {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * SKU名称
     */
    @DecryptField
    private String skuName;

    /**
     * 销售单价
     */
    private BigDecimal salePrice;


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
