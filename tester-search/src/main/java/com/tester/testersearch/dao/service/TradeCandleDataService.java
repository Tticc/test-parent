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
     * 列表查询
     *
     * @param domain 请求对象
     * @return
     */
    List<TradeCandleDataDomain> list(TradeCandleDataDomain domain);
    /* 基础CRUD、分页、列表部分 end */
}