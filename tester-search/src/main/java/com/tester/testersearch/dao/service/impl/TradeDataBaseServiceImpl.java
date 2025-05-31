package com.tester.testersearch.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.config.sharding.properties.ShardingDatabaseProperties;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.mapper.TradeDataBaseMapper;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

    @Autowired
    private ShardingDatabaseProperties databaseProperties;

    private int defaultCacheSize = 1000;

    private AtomicLong minId = new AtomicLong(1L);
    private AtomicLong maxId = new AtomicLong(1L);
    private List<TradeSignDTO> cacheList = new ArrayList<>(defaultCacheSize+100);

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
                    && null != (maxId = this.getMaxIdByDs(request.getBKey())) && maxId > request.getId()) {
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
    public PageInfo<TradeSignDTO> listPageWithCache(TradeDataBasePageRequest request, TradeParam tradeParam) {
        if(null == request.getId()){
            return null;
        }
        Long id = request.getId();
        int pageSize = request.getPageSize();
        TradeDataBasePageRequest tempRequest = new TradeDataBasePageRequest();
        BeanUtils.copyProperties(request,tempRequest);
        PageInfo<TradeSignDTO> pageInfo;
        try {
            if(tempRequest.getEndId() == null) {
                if(tempRequest.getPageSize() > 0){
                    tempRequest.setEndId(tempRequest.getId() + (tempRequest.getPageSize()+10)*1000);
                }else {
                    tempRequest.setEndId(new Date().getTime());
                }
            }
            if(tempRequest.getPageSize() < defaultCacheSize){
                if(tempRequest.getId() > minId.get() && tempRequest.getEndId() < maxId.get()){
                    List<TradeSignDTO> collect = this.returnList(cacheList, id, pageSize);
                    pageInfo = new PageInfo<TradeSignDTO>();
                    pageInfo.setList(collect);
                    return pageInfo;
                }
                tempRequest.setPageSize(defaultCacheSize);
                tempRequest.setEndId(Math.max(tempRequest.getId() + (tempRequest.getPageSize()+10)*1000,tempRequest.getEndId()));
            }
            pageInfo = PageHelper.startPage(tempRequest.getPageNum(), tempRequest.getPageSize(),false)
                    .doSelectPageInfo(() -> tradeDataBaseMapper.listAfter(tempRequest));

            // 如果结果为空，且当前请求的id小于最大id，加大范围（一个月）重试一次。
            // 去除查询最大id逻辑，因为ShardingSphere会全表扫描，耗时过大 2025年5月17日
            Long maxId;
            if (null != pageInfo && CollectionUtils.isEmpty(pageInfo.getList())
//                    && null != (maxId = tradeDataBaseMapper.getMaxId(request.getBKey())) && maxId > tempRequest.getId()
            ) {
                LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(tempRequest.getId()), ZoneId.systemDefault());
                endTime = endTime.plusMonths(1);
                Date endDate = DateUtil.getDateFromLocalDateTime(endTime);
                tempRequest.setEndId(endDate.getTime());
                pageInfo = PageHelper.startPage(tempRequest.getPageNum(), tempRequest.getPageSize(), false)
                        .doSelectPageInfo(() -> tradeDataBaseMapper.listAfter(tempRequest));
            }
        } catch (Exception e) {
            log.error("Error fetching paginated data", e);
            throw e; // 或者其它适当的错误处理
        } finally {
            PageHelper.clearPage(); // 清理 ThreadLocal
        }
        if(!CollectionUtils.isEmpty(pageInfo.getList())){
            cacheList = pageInfo.getList();
            LongSummaryStatistics stats = pageInfo.getList().stream()
                    .mapToLong(TradeSignDTO::getId)
                    .summaryStatistics();
            minId.set(stats.getMin());
            maxId.set(stats.getMax());
            List<TradeSignDTO> tradeSignDTOS = this.returnList(cacheList, id, pageSize);
            pageInfo.setList(tradeSignDTOS);
        }
        return pageInfo;
    }

    private List<TradeSignDTO> returnList(List<TradeSignDTO> list, Long minId, int size){
        return list.stream().filter(obj -> obj.getId() >= minId)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public int batchSave(List<TradeDataBaseDomain> entities) {
        return tradeDataBaseMapper.batchSave(entities);
    }

    @Override
    public Long getMinId(String bKey) {
        return tradeDataBaseMapper.getMinId(bKey);
    }

    @Override
    public Long getMaxId(String bKey) {
        return tradeDataBaseMapper.getMaxId(bKey);
    }

    /**
     * 利用范围分表规则，较少需要查询的表，优化查询速度
     * @param bKey
     * @return
     */
    @Override
    public Long getMaxIdByDs(String bKey) {
        long time = new Date().getTime()+(60*60*1000);
        for (int i = 0; i >= -12 ; i--) {
            LocalDateTime firstDayOfMonth = LocalDate.now()
                    .plusMonths(i)
                    .withDayOfMonth(1)         // 设置为本月第一天
                    .atStartOfDay();           // 设置时间为00:00:00
            // 转换为时间戳（毫秒）
            long timestamp = firstDayOfMonth
                    .atZone(ZoneId.systemDefault())  // 使用系统默认时区
                    .toInstant()
                    .toEpochMilli();
            Long maxIdByDs = tradeDataBaseMapper.getMaxIdByDs(bKey, timestamp,time);
            if(null != maxIdByDs){
                return maxIdByDs;
            }
        }
        return null;
    }
}