/*
 * 版权所有.
 */

package com.tester.testersearch.dao.mapper;

import com.tester.base.dto.dao.BaseMapper;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 蜡烛交易信号数据Mapper接口
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
@Mapper
public interface CandleTradeSignalMapper extends BaseMapper<CandleTradeSignalDomain, Long> {

    @Deprecated
    int delete(Long id);

    int getByBKey(@Param("id")Long id, @Param("bKey") String bKey);

    @Deprecated
    CandleTradeSignalDomain get(Long id);

    int deleteByBKey(@Param("id")Long id, @Param("bKey") String bKey);

    /**
     * 列表查询
     *
     * @param domain
     * @return List<CandleTradeSignalDomain>
     */
    List<CandleTradeSignalDomain> list(CandleTradeSignalDomain domain);

    /**
     *
     * @param bKey
     * @param bar
     * @param openTimestamp
     * @return
     */
    CandleTradeSignalDomain getByTimestamp(@Param("bKey") String bKey,
                                           @Param("bar") String bar,
                                           @Param("step") Integer step,
                                           @Param("openTimestamp") Long openTimestamp,
                                           @Param("skipAfterHuge") Integer skipAfterHuge,
                                           @Param("keepSkipAfterHuge") Integer keepSkipAfterHuge,
                                           @Param("skipTimes") BigDecimal skipTimes,
                                           @Param("slTimes") BigDecimal slTimes,
                                           @Param("tpTimes") BigDecimal tpTimes
    );

    /**
     * 批量插入
     *
     * @param entities
     * @return int
     */
    int batchSave(List<CandleTradeSignalDomain> entities);
}