package com.tester.testermybatis.dao.mapper;

import com.tester.testercommon.dao.BaseMapper;
import com.tester.testermybatis.dao.domain.OrderItemDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItemDomain, Long> {

    List<OrderItemDomain> listByOrderNo(@Param("orderNo") Long orderNo);
}
