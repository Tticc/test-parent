package com.tester.testersearch.config.schedule.binc;

import com.github.pagehelper.PageInfo;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.BinCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取交易数据定时任务
 * wenc
 */
@Slf4j
//@Component
public class FetchBinanceDataJob {


    @Autowired
    private TradeDataBaseService tradeDataBaseService;

    // 每隔 5分钟 执行一次
    @Scheduled(fixedRate = 20 * 60 * 1000)
    public void performTask() {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("查询最大id");
            int batchSize = 500;
            Long maxId = tradeDataBaseService.getMaxId();
            stopWatch.stop();
            if(null == maxId){
                maxId = new Date().getTime()-60*60*1000;
            }
            stopWatch.start("取最近数据fetchDataAfter");
//            this.fetchDataAfter(maxId, batchSize);
            stopWatch.stop();
            stopWatch.start("查询最小id");
            Long minId = tradeDataBaseService.getMinId();
            stopWatch.stop();
            stopWatch.start("取历史数据fetchDataAfter");
            this.fetchDataBefore(minId, batchSize, 5000);
            stopWatch.stop();

//            // 检查所有数据
//            stopWatch.start("检查所有数据");
//            this.checkAllData();
//            stopWatch.stop();

            System.out.println("stopWatch.prettyPrint() = " + stopWatch.prettyPrint());
        } catch (Exception e) {
            log.error("定时任务执行失败。", e);
        }
    }


    private void fetchDataBefore(Long endAt, int pageSize, int totalSize) {
        long startTime = endAt;
        long stopTime = endAt - (totalSize * 1000);
        long startAt;
        do {
            startAt = endAt - pageSize * 1000;
            List<TradeDataBaseDomain> dataList = BinCommon.fetchData(BarEnum._1s.getCode(), pageSize + "", startAt + "", endAt + "");
            endAt = startAt;
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }
            tradeDataBaseService.batchSave(dataList);
        } while (startAt > stopTime);
        this.checkPartData(stopTime, startTime, pageSize);
    }

    private void fetchDataAfter(Long startAt, int pageSize) {
        long startTime = startAt;
        long endTime = new Date().getTime();
        long endAt = startAt;
        do {
            endAt += pageSize * 1000;
            List<TradeDataBaseDomain> dataList = BinCommon.fetchData(BarEnum._1s.getCode(), pageSize + "", startAt + "", endAt + "");
            startAt = endAt;
            if (CollectionUtils.isEmpty(dataList)) {
                continue;
            }
            tradeDataBaseService.batchSave(dataList);
        } while (endAt < endTime);
        this.checkPartData(startTime, endTime, pageSize);
    }

    private void checkPartData(Long startId, Long endId, int pageSize) {
        List<Long> lackData = new ArrayList<>();
        long minId = startId;
        do{
            TradeDataBasePageRequest req = new TradeDataBasePageRequest();
            req.setId(minId);
            req.setPageNum(1);
            req.setPageSize(pageSize);
            PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPage(req);
            if (null != pageInfo && !CollectionUtils.isEmpty(pageInfo.getList())) {
                List<TradeSignDTO> list = pageInfo.getList();
                Set<Long> collect = list.stream().map(e -> e.getId()).collect(Collectors.toSet());
                boolean hasNextPage = list.size() >= pageSize;
                for (int i = 0; i < (hasNextPage ? pageSize : list.size()); i++) {
                    boolean contains = collect.contains(minId);
                    if (!contains) {
                        lackData.add(minId);
                    }
                    minId += 1000;
                }
            }
            minId+=pageSize*1000;
        }while (minId < endId);
        if (!CollectionUtils.isEmpty(lackData)) {
            log.warn("lackData数量:{}。起始位置:{}", lackData.size(), lackData.get(0));
            for (Long lackDatum : lackData) {
                this.fetchDataBefore(lackDatum + 2, 10, 5);
            }
        }else{
            log.warn("检查完成，数据无缺失。开始时间:{},结束时间:{}", DateUtil.dateFormat(new Date(startId)), DateUtil.dateFormat(new Date(endId)));
        }
    }
    private void checkAllData() {
        List<Long> lackData = new ArrayList<>();
        Long minId = tradeDataBaseService.getMinId();
        boolean hasNextPage = false;
        int pageSize = 100;
        do {
            hasNextPage = false;
            TradeDataBasePageRequest req = new TradeDataBasePageRequest();
            req.setId(minId);
            req.setPageNum(1);
            req.setPageSize(pageSize);
            PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPage(req);
            if (null != pageInfo && !CollectionUtils.isEmpty(pageInfo.getList())) {
                List<TradeSignDTO> list = pageInfo.getList();
                Set<Long> collect = list.stream().map(e -> e.getId()).collect(Collectors.toSet());
                hasNextPage = list.size() >= pageSize;
                for (int i = 0; i < (hasNextPage ? pageSize : list.size()); i++) {
                    boolean contains = collect.contains(minId);
                    if (!contains) {
                        lackData.add(minId);
                    }
                    minId += 1000;
                }
            }
        } while (hasNextPage);
        if (!CollectionUtils.isEmpty(lackData)) {
            log.warn("lackData数量:{}。起始位置:{}", lackData.size(), lackData.get(0));
            for (Long lackDatum : lackData) {
                this.fetchDataBefore(lackDatum + 2, 10, 5);
            }
        }
        log.info("数据检查完成");
    }
}
