/*
* 版权所有.
*/

package com.tester.testersearch.dao.service;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.dao.BaseService;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;

import java.util.List;

/**
 * 蜡烛数据业务服务接口
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-17
 */
public interface TradeCandleDataService extends BaseService<Long,TradeCandleDataDomain> {
    /* 基础CRUD、分页、列表部分 begin */
    /**
     * 分页查询
     *
     * @param request 分页查询请求对象
     * @return
     */
    PageInfo<TradeCandleDataDomain> listPage(TradeCandleDataPageRequest request);

    /**
     * 根据timestamp获取记录
     * @param bKey
     * @param bar
     * @param openTimestamp
     * @return
     */
    TradeCandleDataDomain getByTimestamp(String bKey, String bar, Long openTimestamp);

    /**
     * 批量保存
     * @param entities
     * @return
     */
    int batchSave(List<TradeCandleDataDomain> entities);
    /* 基础CRUD、分页、列表部分 end */
}