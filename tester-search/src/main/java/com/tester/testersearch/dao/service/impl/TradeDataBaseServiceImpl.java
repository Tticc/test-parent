package com.tester.testersearch.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.mapper.TradeDataBaseMapper;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
        if(null == request.getId()){
            return null;
        }
        PageInfo<TradeSignDTO> pageInfo;
        try {
            if(request.getEndId() == null) {
                if(request.getPageSize() > 0){
                    request.setEndId(request.getId() + (request.getPageSize()+10)*1000);
                }else {
                    request.setEndId(new Date().getTime());
                }
            }
            pageInfo = PageHelper.startPage(request.getPageNum(), request.getPageSize(),false)
                    .doSelectPageInfo(() -> tradeDataBaseMapper.listAfter(request));

            // 如果结果为空，且当前请求的id小于最大id，加大范围（一个月）重试一次。
            Long maxId;
            if (null != pageInfo && CollectionUtils.isEmpty(pageInfo.getList())
                    && null != (maxId = tradeDataBaseMapper.getMaxId()) && maxId > request.getId()) {
                LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getId()), ZoneId.systemDefault());
                endTime = endTime.plusMonths(1);
                Date endDate = DateUtil.getDateFromLocalDateTime(endTime);
                request.setEndId(endDate.getTime());
                pageInfo = PageHelper.startPage(request.getPageNum(), request.getPageSize(), false)
                        .doSelectPageInfo(() -> tradeDataBaseMapper.listAfter(request));
            }
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