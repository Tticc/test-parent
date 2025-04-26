package com.tester.testersearch.config.schedule.binc;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.DetectTradeSign15_25min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易测试
 * wenc
 */
@Slf4j
//@Component
public class TradeTestJob {

    @Autowired
    private BinanceHelper binanceHelper;


    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
        String startAt = "20250401000000";
        int step = 5;
        BarEnum barEnum = BarEnum._30m;
        binanceHelper.traceLocal(startAt,80, step, barEnum);
        Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
        List<TradeSignDTO> tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
        do {
            DetectTradeSign15_25min.createChart(tradeSignDTOS);
//            DetectTradeSign15_25min_anti.createChart(tradeSignDTOS);
            binanceHelper.traceLocal(startAt,1, step, barEnum);
            hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
            tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
        }while (!CollectionUtils.isEmpty(tradeSignDTOS));

    }
}
