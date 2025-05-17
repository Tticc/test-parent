package com.tester.testersearch.dao.mapper;

import com.tester.base.dto.dao.BaseMapper;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易数据Mapper接口
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2025-03-16
 */
@Mapper
public interface TradeDataBaseMapper extends BaseMapper<TradeDataBaseDomain, Long> {
    /**
     * 列表查询
     *
     * @param request
     * @return List<TradeDataBaseDomain>
     */
    List<TradeDataBaseDomain> list(TradeDataBasePageRequest request);

    List<TradeSignDTO> listAfter(TradeDataBasePageRequest request);

    /**
     * 批量插入
     *
     * @param entities
     * @return int
     */
    int batchSave(List<TradeDataBaseDomain> entities);

    Long getMinId(@Param("bKey") String bKey);

    Long getMaxId(@Param("bKey") String bKey);
}