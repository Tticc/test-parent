package com.tester.testersearch.config.schedule.binc;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.model.CandleTradeSignalPageRequest;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;
import com.tester.testersearch.dao.service.CandleTradeSignalService;
import com.tester.testersearch.dao.service.TradeCandleDataService;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.StrategyEnum;
import com.tester.testersearch.util.binc.sharp.DailyReturnAggregator;
import com.tester.testersearch.util.binc.sharp.SharpeRatioCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试
 * wenc
 */
@Slf4j
//@Component
public class Testing {

    @Autowired
    private TradeCandleDataService tradeCandleDataService;
    @Autowired
    private CandleTradeSignalService candleTradeSignalService;

    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
        TradeParam tradeParam = StrategyEnum._1000160.getParam();
        CandleTradeSignalPageRequest request = new CandleTradeSignalPageRequest();
        request.setBKey(BKeyEnum.BTCUSDT.getCode())
                .setActualTrade(1)
//                .setTradeEnd(1)
                .setStrategyCode(tradeParam.getStrategyCode());
        request.setPageNum(1);
        request.setPageSize(100000);
        PageInfo<CandleTradeSignalDomain> candleTradeSignalDomainPageInfo = candleTradeSignalService.listPage(request);
        List<CandleTradeSignalDomain> list = candleTradeSignalDomainPageInfo.getList();
        System.out.println("list.size() = " + list.size());
        System.out.println("sum = " + list.stream().map(e -> e.getTradeProfitsRate()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));

        List<DailyReturnAggregator.TradeProfit> collect = list.stream().map(e -> {
            DailyReturnAggregator.TradeProfit tradeProfit = new DailyReturnAggregator.TradeProfit(e.getOpenTimestamp(), e.getTradeProfitsRate());
            return tradeProfit;
        }).collect(Collectors.toList());
        Map<LocalDate, BigDecimal> localDateBigDecimalMap = DailyReturnAggregator.aggregateDailyReturns(collect);
        BigDecimal annualRiskFreeRate = new BigDecimal("0.03"); // 3%
        BigDecimal sharpe = SharpeRatioCalculator.calculateSharpeRatio(localDateBigDecimalMap.values().stream().collect(Collectors.toList()), annualRiskFreeRate);
        System.out.println("Sharpe Ratio: " + sharpe);

    }
}
