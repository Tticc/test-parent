package com.tester.testersearch.dao.mapper;

import com.tester.base.dto.dao.BaseMapper;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 蜡烛数据Mapper接口
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-17
 */
@Mapper
public interface TradeCandleDataMapper extends BaseMapper<TradeCandleDataDomain, Long> {
    /**
     * 列表查询
     *
     * @param domain
     * @return List<TradeCandleDataDomain>
     */
    List<TradeCandleDataDomain> list(TradeCandleDataDomain domain);

    /**
     * 批量插入
     *
     * @param entities
     * @return int
     */
    int batchSave(List<TradeCandleDataDomain> entities);
}