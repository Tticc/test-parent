package com.tester.testersearch.config.schedule.binc;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.service.TradeCandleDataService;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.BarEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 测试
 * wenc
 */
@Slf4j
//@Component
public class FulfillTradeData {

    @Autowired
    private TradeCandleDataService tradeCandleDataService;
    @Autowired
    protected BinanceHelper binanceHelper;

    Map<Long, TradeCandleDataDomain> cacheMap = new LinkedHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("测试开始");
        TradeParam tradeParam = new TradeParam();
        tradeParam
                .setBKey(BKeyEnum.BTCUSDT.getCode())
//                .setBKey(BKeyEnum.ETHUSDT.getCode())
                .setStep(100)
                .setBarEnum(BarEnum._30m)
                .setSkipAfterHuge(10)
                .setKeepSkipAfterHuge(10)
                .setSlTimes(new BigDecimal("0.01"))
                .setTpTimes(new BigDecimal("0.07"))
                .setSkipTimes(new BigDecimal("0.012"))
                .setReverseSlTimes(new BigDecimal("0.005"))
                .setReverseTpTimes(new BigDecimal("0.01"))
                .setReverseSkipNum(0)
                .setReverseTakeNum(0);
        this.fetchData("20191225000000", "20260105000000", tradeParam);
        stopWatch.stop();
        log.info("测试完成。耗时：{}", stopWatch.prettyPrint());
    }

    private void fetchData(String startAt, String endAt, TradeParam tradeParam) throws BusinessException {
        Long maxId = Long.MAX_VALUE;
        if (!StringUtils.isEmpty(endAt)) {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(endAt);
            Date endDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            maxId = endDate.getTime();
        }
        List<TradeSignDTO> resList;
        AtomicBoolean ifLastAto = new AtomicBoolean(false);
        resList = binanceHelper.traceLocal(tradeParam,startAt, 80, (ifLast) -> {}, (newCandle) -> {}, true);
        this.saveList(resList, tradeParam);

        long lastUpdateTimestamp = 0L;
        do {
            resList = binanceHelper.traceLocal(tradeParam,startAt, 1, (ifLast) -> ifLastAto.set(ifLast), (newCandle) -> {}, true);
            TradeSignDTO tradeSignDTO = resList.get(resList.size() - 1);
            lastUpdateTimestamp = tradeSignDTO.getLastUpdateTimestamp();
            this.saveList(resList, tradeParam);
        } while (!ifLastAto.get() && lastUpdateTimestamp < maxId);
        this.updateList(tradeParam);
    }

    private void saveList(List<TradeSignDTO> resList, TradeParam tradeParam) {
        if (CollectionUtils.isEmpty(resList)) {
            return;
        }
        resList.stream().limit(Math.max(0,resList.size()-1)).forEach(e -> e.setCompleted(1));
        for (TradeSignDTO res : resList) {
            TradeCandleDataDomain candle = cacheMap.get(res.getOpenTimestamp());
            if (null == candle) {
                TradeCandleDataDomain saveDomain = new TradeCandleDataDomain();
                saveDomain.init();
                saveDomain.setBar(tradeParam.getBarEnum().getCode())
                        .setBKey(tradeParam.getBKey())
                        .setOpen(res.getOpen())
                        .setClose(res.getClose())
                        .setHigh(res.getHigh())
                        .setLow(res.getLow())
                        .setOpenTimestamp(res.getOpenTimestamp())
                        .setEndTimestamp(res.getEndTimestamp())
                        .setLastUpdateTimestamp(res.getLastUpdateTimestamp())
                        .setVolume(res.getVolume())
                        .setMa5(res.getMa5())
                        .setMa10(res.getMa10())
                        .setMa20(res.getMa20())
                        .setCompleted(res.getCompleted())
                        .setExtColumn(null);
                cacheMap.put(res.getOpenTimestamp(), saveDomain);
            } else {
                candle.setLastUpdateTimestamp(res.getLastUpdateTimestamp())
                        .setVolume(res.getVolume())
                        .setMa5(res.getMa5())
                        .setMa10(res.getMa10())
                        .setMa20(res.getMa20())
                        .setOpen(res.getOpen())
                        .setClose(res.getClose())
                        .setHigh(res.getHigh())
                        .setLow(res.getLow())
                        .setCompleted(res.getCompleted())
                        .setOpenTimestamp(res.getOpenTimestamp())
                        .setEndTimestamp(res.getEndTimestamp())
                        .setUpdateTime(new Date());
            }
        }
        if (cacheMap.size() > 100) {
            this.updateList(tradeParam);
        }
    }

    private void updateList(TradeParam tradeParam) {
        List<TradeCandleDataDomain> collect = cacheMap.values().stream().collect(Collectors.toList());
        TradeCandleDataDomain last = collect.get(collect.size() - 1);
        cacheMap = new LinkedHashMap<>();
        cacheMap.put(last.getOpenTimestamp(), last);
        List<TradeCandleDataDomain> saveList = new ArrayList<>();
        List<TradeCandleDataDomain> updateList = new ArrayList<>();
        for (TradeCandleDataDomain tradeCandleDataDomain : collect) {
            TradeCandleDataDomain byTimestamp = tradeCandleDataService.getByTimestamp(tradeParam.getBKey(), tradeParam.getBarEnum().getCode(), tradeCandleDataDomain.getOpenTimestamp());
            if (null == byTimestamp) {
                saveList.add(tradeCandleDataDomain);
            } else {
                // 只更新未完成的蜡烛
                if(!Objects.equals(byTimestamp.getCompleted(),1)) {
                    tradeCandleDataDomain.setId(byTimestamp.getId());
                    updateList.add(tradeCandleDataDomain);
                }
            }
        }
        if (!CollectionUtils.isEmpty(saveList)) {
            tradeCandleDataService.batchSave(saveList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            for (TradeCandleDataDomain tradeCandleDataDomain : updateList) {
                tradeCandleDataService.update(tradeCandleDataDomain);
            }
        }
    }
}
