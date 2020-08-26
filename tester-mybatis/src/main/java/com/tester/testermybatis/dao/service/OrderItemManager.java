package com.tester.testermybatis.dao.service;

import com.tester.testermybatis.dao.domain.OrderItemDomain;
import com.tester.testermybatis.dao.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2020-8-25 13:56:56
 */
@Service
public class OrderItemManager {
    @Resource
    private OrderItemMapper orderItemMapper;

    public List<OrderItemDomain> listByOrderNo(Long orderNo){
        return orderItemMapper.listByOrderNo(orderNo);
    }


    /***************** tk.mybatis **************************************************/
    public int insert(OrderItemDomain domain){
        int insert = orderItemMapper.insert(domain);
        return insert;
    }

}
