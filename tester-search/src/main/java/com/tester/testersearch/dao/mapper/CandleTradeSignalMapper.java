/*
 * 版权所有.
 */

package com.tester.testersearch.dao.mapper;

import com.tester.base.dto.dao.BaseMapper;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import org.apache.ibatis.annotations.Mapper;

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
    /**
     * 列表查询
     *
     * @param domain
     * @return List<CandleTradeSignalDomain>
     */
    List<CandleTradeSignalDomain> list(CandleTradeSignalDomain domain);

    /**
     * 批量插入
     *
     * @param entities
     * @return int
     */
    int batchSave(List<CandleTradeSignalDomain> entities);
}