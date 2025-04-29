package com.tester.testersearch.service.binc.strategy;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.binance.DetectTradeSign;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
import com.tester.testersearch.util.binc.tradeSign.PrinterHelper;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 根据MA线交策略
 */
@Service
@Slf4j
public class MACrossStrategy {
    @Autowired
    protected BinanceHelper binanceHelper;

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
            binanceHelper.traceLocal(startAt, 80, step, barEnum, (ifLast) -> {}, false);
            Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
            List<TradeSignDTO> tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            AtomicBoolean ifLastAto = new AtomicBoolean(false);
            long lastUpdateTimestamp = 0L;
            int size = 0;
            do {
                List<TradeSignDTO> tradeList = tradeSignDTOS.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
                if(tradeList.size() != size){
                    size = tradeList.size();
                    PrinterHelper.printProfits(tradeList);
                }
                TradeSignDTO tradeSignDTO = tradeSignDTOS.get(tradeSignDTOS.size() - 1);
                lastUpdateTimestamp = tradeSignDTO.getLastUpdateTimestamp();
                binanceHelper.traceLocal(startAt, 1, step, barEnum, (ifLast) -> {
                    ifLastAto.set(ifLast);
                }, false);
                hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
                tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            } while (!ifLastAto.get() && lastUpdateTimestamp < maxId);
        } catch (Exception e) {
            log.error("测试异常。err:", e);
        }
    }


    /**
     * 计算并设置MA交易信号
     * @param tradeSignList
     * @param excludeLast
     * @throws BusinessException
     */
    public static void calculateTradeSign(List<TradeSignDTO> tradeSignList, boolean excludeLast) throws BusinessException {
        TradeSignDTO lastCalcTradeSignDTO = tradeSignList.get(tradeSignList.size() - 1);
        TradeSignDTO lastTradeSignDTO = tradeSignList.get(tradeSignList.size() - 1);
        List<TradeSignDTO> tempTradeSignList = tradeSignList;
        if(excludeLast){
            tempTradeSignList = tempTradeSignList.stream().limit(tempTradeSignList.size()-1).collect(Collectors.toList());
            lastCalcTradeSignDTO = tempTradeSignList.get(tempTradeSignList.size() - 1);
        }
        if (tempTradeSignList.get(0).getTradeSign() == null) {
            MAUtil.initTradeSignM5M20(tempTradeSignList);
        }
        Boolean lastBuySign = null;
        for (TradeSignDTO value : tempTradeSignList) {
            if (Objects.equals(value.getTradeSign(), TradeSignEnum.BUY_SIGN.getCode())) {
                lastBuySign = true;
            } else if (Objects.equals(value.getTradeSign(), TradeSignEnum.SELL_SIGN.getCode())) {
                lastBuySign = false;
            }
        }
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(lastTradeSignDTO)) {
            return;
        }
        // 默认使用最新价格作为成交价格
        // BigDecimal tradePrice = tradeSignDTO.getOpen();
        BigDecimal tradePrice = lastTradeSignDTO.getClose();

        // 交易信号到来
        boolean tradeSignCome = false;
        if (lastCalcTradeSignDTO.getMa5().compareTo(lastCalcTradeSignDTO.getMa20()) > 0 && (null == lastBuySign || !lastBuySign)) {
            // 如果上穿，且最近一次信号是sell。设置此次信号为BUY
            lastTradeSignDTO.setTradeSign(TradeSignEnum.BUY_SIGN.getCode());
            lastTradeSignDTO.setTradePrice(tradePrice);
            lastTradeSignDTO.setTradeTime(new Date(lastTradeSignDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            log.info("交易信号来临。buy。交易时间:{},交易价格:{}",DateUtil.dateFormat(lastTradeSignDTO.getTradeTime()),DecimalUtil.format(lastTradeSignDTO.getTradePrice()));
        } else if (lastCalcTradeSignDTO.getMa5().compareTo(lastCalcTradeSignDTO.getMa20()) < 0 && (null == lastBuySign || lastBuySign)) {
            // 如果下穿，且最近一次信号是buy。设置此次信号为SELL
            lastTradeSignDTO.setTradeSign(TradeSignEnum.SELL_SIGN.getCode());
            lastTradeSignDTO.setTradePrice(tradePrice);
            lastTradeSignDTO.setTradeTime(new Date(lastTradeSignDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            log.info("交易信号来临。sell。交易时间:{},交易价格:{}",DateUtil.dateFormat(lastTradeSignDTO.getTradeTime()),DecimalUtil.format(lastTradeSignDTO.getTradePrice()));
        } else {
            lastTradeSignDTO.setTradeSign(TradeSignEnum.NONE_SIGN.getCode());
        }
    }
}
