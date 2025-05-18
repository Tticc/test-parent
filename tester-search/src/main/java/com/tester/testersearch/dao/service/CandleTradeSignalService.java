/*
 * 版权所有.
 */

package com.tester.testersearch.dao.service;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.dao.BaseService;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import com.tester.testersearch.dao.model.CandleTradeSignalPageRequest;
import com.tester.testersearch.service.binc.strategy.TradeParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 蜡烛交易信号数据业务服务接口
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
public interface CandleTradeSignalService extends BaseService<Long, CandleTradeSignalDomain> {
    /* 基础CRUD、分页、列表部分 begin */

    /**
     * 分页查询
     *
     * @param request 分页查询请求对象
     * @return
     */
    PageInfo<CandleTradeSignalDomain> listPage(CandleTradeSignalPageRequest request);


    /**
     * 根据timestamp获取记录
     *
     * @param tradeParam
     * @param openTimestamp
     * @return
     */
    CandleTradeSignalDomain getByTimestamp(TradeParam tradeParam, Long openTimestamp);

    /**
     * 批量保存
     *
     * @param entities
     * @return
     */
    int batchSave(List<CandleTradeSignalDomain> entities);
    /* 基础CRUD、分页、列表部分 end */
}