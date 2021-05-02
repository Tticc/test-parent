package com.tester.testermybatis.dao.service;

import com.tester.testermybatis.model.response.MemberJoinItemVO;
import com.tester.testermybatis.dao.domain.OrderMemberDomain;
import com.tester.testermybatis.dao.mapper.OrderMemberMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2020-8-25 13:56:56
 */
@Service
public class OrderMemberManager {
    @Resource
    private OrderMemberMapper orderMemberMapper;


    public int insert(OrderMemberDomain domain){
        int insert = orderMemberMapper.insert(domain);
        return insert;
    }

    public List<MemberJoinItemVO> testJoinTable(Long mmeberId){
        List<MemberJoinItemVO> memberJoinItemVOS = orderMemberMapper.testJoinTable(mmeberId);
        return memberJoinItemVOS;
    }


    public List<OrderMemberDomain> list(OrderMemberDomain domain){
        return orderMemberMapper.list(domain);
    }

}
