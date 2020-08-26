package com.tester.testermybatis.dao.mapper;

import com.tester.testercommon.dao.BaseMapper;
import com.tester.testermybatis.dao.domain.DemoDomain;

public interface DemoMapper extends BaseMapper<DemoDomain, Long> {
    int batchUpdate(DemoDomain domain);
}
