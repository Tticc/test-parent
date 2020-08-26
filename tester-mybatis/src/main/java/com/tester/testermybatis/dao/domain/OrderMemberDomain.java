package com.tester.testermybatis.dao.domain;

import com.tester.testercommon.dao.BaseDomain;
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

