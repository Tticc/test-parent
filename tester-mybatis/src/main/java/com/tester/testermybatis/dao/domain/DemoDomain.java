package com.tester.testermybatis.dao.domain;

import com.tester.testercommon.dao.BaseDomain;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.Data;

/**
 * @Author 温昌营
 * @Date 2020-8-21 14:27:20
 */
@Data
@DecryptDomain
public class DemoDomain extends BaseDomain {
    @DecryptField
    private String mobile;
}
