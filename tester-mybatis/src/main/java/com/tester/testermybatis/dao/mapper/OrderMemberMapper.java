package com.tester.testermybatis.dao.mapper;

import com.tester.testercommon.dao.BaseMapper;
import com.tester.testermybatis.model.response.MemberJoinItemVO;
import com.tester.testermybatis.dao.domain.OrderMemberDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMemberMapper extends BaseMapper<OrderMemberDomain, Long> {

    List<MemberJoinItemVO> testJoinTable(@Param("memberId") Long memberId);

    List<MemberJoinItemVO> testJoinTable2(@Param("id") Long id);

    int insert(OrderMemberDomain domain);
}
