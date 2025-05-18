/*
 * 版权所有.
 */

package com.tester.testersearch.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import com.tester.testersearch.dao.mapper.CandleTradeSignalMapper;
import com.tester.testersearch.dao.model.CandleTradeSignalPageRequest;
import com.tester.testersearch.dao.service.CandleTradeSignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 蜡烛交易信号数据业务服务实现类
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
@Slf4j
@Service
public class CandleTradeSignalServiceImpl extends BaseServiceImpl<Long, CandleTradeSignalDomain> implements CandleTradeSignalService, InitializingBean {

    @Resource
    private CandleTradeSignalMapper candleTradeSignalMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setBaseMapper(candleTradeSignalMapper);
    }

    @Override
    public PageInfo<CandleTradeSignalDomain> listPage(CandleTradeSignalPageRequest request) {
        CandleTradeSignalDomain domain = new CandleTradeSignalDomain();
        BeanUtils.copyProperties(request, domain);
        PageInfo<CandleTradeSignalDomain> pageInfo = PageHelper.startPage(request.getPageNum(), request.getPageSize(), false)
                .doSelectPageInfo(() -> candleTradeSignalMapper.list(domain));
        return pageInfo;
    }

    @Override
    public List<CandleTradeSignalDomain> list(CandleTradeSignalDomain domain) {
        return null;
    }
}