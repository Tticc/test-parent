package com.tester.testersearch.dao.service;

import com.tester.base.dto.dao.BaseService;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.model.TradeDataBaseResponse;

import java.util.List;

/**
 * 交易数据业务服务接口
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2025-03-16
 */
public interface TradeDataBaseService extends BaseService<Long, TradeDataBaseDomain> {
    /* 基础CRUD、分页、列表部分 begin */

    /**
     * 列表查询
     *
     * @param request 请求对象
     * @return
     */
    List<TradeDataBaseResponse> list(TradeDataBasePageRequest request);

    /**
     * 批量保存
     *
     * @param entities 请求对象
     * @return
     */
    int batchSave(List<TradeDataBaseDomain> entities);

    Long getMinId();

    /* 基础CRUD、分页、列表部分 end */
}