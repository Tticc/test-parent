package com.tester.testersearch.service.binc.strategy;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.DetectTradeSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 根据MA线交策略
 */
@Service
@Slf4j
public class MACrossStrategy {
    @Autowired
    private BinanceHelper binanceHelper;

    /**
     * @param startAt "20250401000000"
     * @param step    5
     * @param barEnum BarEnum._30m
     * @throws BusinessException
     */
    public void runOnce(String startAt, int step, BarEnum barEnum, String endAt) throws BusinessException {
        long maxId = Long.MAX_VALUE;
        if(!StringUtils.isEmpty(endAt)){
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(endAt);
            Date endDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            maxId = endDate.getTime();
        }
        try {
            binanceHelper.traceLocal(startAt, 80, step, barEnum, (ifLast) -> {});
            Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
            List<TradeSignDTO> tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            AtomicBoolean ifLastAto = new AtomicBoolean(false);
            long lastUpdateTimestamp = 0L;
            do {
                DetectTradeSign.createChart(tradeSignDTOS);
                TradeSignDTO tradeSignDTO = tradeSignDTOS.get(tradeSignDTOS.size() - 1);
                lastUpdateTimestamp = tradeSignDTO.getLastUpdateTimestamp();
                binanceHelper.traceLocal(startAt, 1, step, barEnum, (ifLast) -> {
                    ifLastAto.set(ifLast);
                });
                hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
                tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            } while (!ifLastAto.get() && lastUpdateTimestamp < maxId);
        } catch (Exception e) {
            log.error("测试异常。err:", e);
        }
    }
}
