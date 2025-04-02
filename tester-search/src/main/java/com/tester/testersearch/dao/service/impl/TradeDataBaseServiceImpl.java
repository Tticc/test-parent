package com.tester.testersearch.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.mapper.TradeDataBaseMapper;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易数据业务服务实现类
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2025-03-16
 */
@Slf4j
@Service
public class TradeDataBaseServiceImpl extends BaseServiceImpl<Long, TradeDataBaseDomain> implements TradeDataBaseService, InitializingBean {

    @Resource
    private TradeDataBaseMapper tradeDataBaseMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setBaseMapper(tradeDataBaseMapper);
    }

    @Override
    public List<TradeDataBaseDomain> list(TradeDataBasePageRequest request) {
        return tradeDataBaseMapper.list(request);
    }

    @Override
    public PageInfo<TradeSignDTO> listPage(TradeDataBasePageRequest request) {
        PageInfo<TradeSignDTO> pageInfo;
        try {
            pageInfo = PageHelper.startPage(request.getPageNum(), request.getPageSize(),false)
                    .doSelectPageInfo(() -> tradeDataBaseMapper.listAfter(request.getId()));
        } catch (Exception e) {
            log.error("Error fetching paginated data", e);
            throw e; // 或者其它适当的错误处理
        } finally {
            PageHelper.clearPage(); // 清理 ThreadLocal
        }
        return pageInfo;
    }

    @Override
    public int batchSave(List<TradeDataBaseDomain> entities) {
        return tradeDataBaseMapper.batchSave(entities);
    }

    @Override
    public Long getMinId() {
        return tradeDataBaseMapper.getMinId();
    }

    @Override
    public Long getMaxId() {
        return tradeDataBaseMapper.getMaxId();
    }
}