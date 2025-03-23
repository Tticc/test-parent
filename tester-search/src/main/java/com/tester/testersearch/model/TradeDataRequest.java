package com.tester.testersearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * knowledge实体类
 *
 * @Date 14:13 2022/8/23
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class TradeDataRequest {
    /**
     * 数量
     **/
    private Integer limit;

    /**
     * 分页
     **/
    private Integer page;
}