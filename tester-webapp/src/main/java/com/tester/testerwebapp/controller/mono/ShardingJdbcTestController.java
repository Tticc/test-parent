package com.tester.testerwebapp.controller.mono;


import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.MemberRequest;
import com.tester.testermybatis.dao.domain.OrderItemDomain;
import com.tester.testermybatis.dao.domain.OrderMemberDomain;
import com.tester.testermybatis.dao.service.OrderItemManager;
import com.tester.testermybatis.dao.service.OrderMemberManager;
import com.tester.testermybatis.service.MyKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/sharding")
@Slf4j
public class ShardingJdbcTestController extends BaseController {
    @Autowired
    private OrderMemberManager orderMemberManager;

    @Autowired
    private OrderItemManager orderItemManager;

    @Autowired
    private MyKeyGenerator myKeyGenerator;


    @PostMapping(value = "/insertOrderItem")
    public RestResult<OrderItemDomain> insertOrderItem(@RequestBody @Validated MemberRequest request) throws BusinessException {
        Long memberId = request.getMemberId();
        Long aLong = myKeyGenerator.generateOrderNo(memberId, 1);
        OrderItemDomain domain = new OrderItemDomain().init();
        domain.setMemberId(memberId)
                .setOrderNo(aLong)
                .setProductId(0L)
                .setSkuId(0L)
                .setSkuName("name")
                .setSaleQty(new BigDecimal("0.00"))
                .setSalePrice(new BigDecimal("0.00"))
                .setSaleUnit("zhang")
                .setWeight(new BigDecimal("0.00"));
        int insert = orderItemManager.save(domain);

        OrderMemberDomain orderMemberDomain = new OrderMemberDomain().init();
        orderMemberDomain.setMemberAccount("account129039")
                .setMemberId(memberId)
                .setMemberPhone("" + memberId)
                .setOrderNo(aLong);
        int insert1 = orderMemberManager.save(orderMemberDomain);
        return success(domain);
    }

    @PostMapping(value = "/listByPhone")
    public RestResult<List<OrderMemberDomain>> listByPhone() throws BusinessException {
        OrderMemberDomain listDomain = new OrderMemberDomain();

        // 查询非分库分表映射字段是，会全库全表扫描。
//        listDomain.setMemberPhone("8374924232");

        // 查询分库分表映射字段是，会根据策略查到具体表
        // 分表策略 com.tester.testermybatis.config.prop.ComplexTableShardingAlgorithm
        listDomain.setOrderNo(315418401587201001L);
        List<OrderMemberDomain> list = orderMemberManager.list(listDomain);
        return success(list);
    }


}
