/*
* 版权所有.
*/

package com.tester.testersearch.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import lombok.extern.slf4j.Slf4j;
import com.tester.testersearch.dao.mapper.TradeCandleDataMapper;
import com.tester.testersearch.dao.service.TradeCandleDataService;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.util.List;
/**
 * 蜡烛数据业务服务实现类
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-17
 */
@Slf4j
@Service
public class TradeCandleDataServiceImpl extends BaseServiceImpl<Long,TradeCandleDataDomain> implements TradeCandleDataService, InitializingBean {

    @Resource
    private TradeCandleDataMapper tradeCandleDataMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setBaseMapper(tradeCandleDataMapper);
    }

    @Override
    public PageInfo<TradeCandleDataDomain> listPage(TradeCandleDataPageRequest request){
        TradeCandleDataDomain domain = new TradeCandleDataDomain();
        BeanUtils.copyProperties(request, domain);
        PageInfo<TradeCandleDataDomain> pageInfo= PageHelper.startPage(request.getPageNum(), request.getPageSize(),false)
                .doSelectPageInfo(() -> tradeCandleDataMapper.list(domain));
        return pageInfo;
    }
    @Override
    public List<TradeCandleDataDomain> list(TradeCandleDataDomain domain){
        return null;
    }
}