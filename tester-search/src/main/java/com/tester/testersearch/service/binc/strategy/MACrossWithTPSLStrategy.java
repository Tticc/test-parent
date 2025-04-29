package com.tester.testersearch.service.binc.strategy;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
import com.tester.testersearch.util.binc.tradeSign.PrinterHelper;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * ma加上止盈止损
 * TP (Take Profit) 和 SL (Stop Loss)
 */
@Service
@Slf4j
public class MACrossWithTPSLStrategy {
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
        if (!StringUtils.isEmpty(endAt)) {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(endAt);
            Date endDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            maxId = endDate.getTime();
        }
        try {
            binanceHelper.traceLocal(startAt, 80, step, barEnum, (ifLast) -> {
            }, true);
            Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
            List<TradeSignDTO> tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            AtomicBoolean ifLastAto = new AtomicBoolean(false);
            long lastUpdateTimestamp = 0L;
            int size = 0;
            do {
                List<TradeSignDTO> tradeList = tradeSignDTOS.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
                if (tradeList.size() != size) {
                    size = tradeList.size();
                    PrinterHelper.printProfitsWithTPSL(tradeList);
                }
                TradeSignDTO tradeSignDTO = tradeSignDTOS.get(tradeSignDTOS.size() - 1);
                lastUpdateTimestamp = tradeSignDTO.getLastUpdateTimestamp();
                binanceHelper.traceLocal(startAt, 1, step, barEnum, (ifLast) -> {
                    ifLastAto.set(ifLast);
                }, true);
                hisDataMap = BinanceHelper.getByBarEnumCode(barEnum.getCode());
                tradeSignDTOS = hisDataMap.values().stream().sorted(Comparator.comparing(e -> e.getTimestamp())).collect(Collectors.toList());
            } while (!ifLastAto.get() && lastUpdateTimestamp < maxId);
        } catch (Exception e) {
            log.error("测试异常。err:", e);
        }
    }


    /**
     * 计算并设置MA交易信号
     *
     * @param tradeSignList
     * @throws BusinessException
     */
    public static void calculateTradeSign_excludeLast(List<TradeSignDTO> tradeSignList) throws BusinessException {

        BigDecimal stopLossNum = new BigDecimal(800);
        BigDecimal takeProfitNum = new BigDecimal(6000);

        TradeSignDTO lastCandleDTO = tradeSignList.get(tradeSignList.size() - 1);
        List<TradeSignDTO> tempTradeSignList = tradeSignList.stream().limit(tradeSignList.size() - 1).collect(Collectors.toList());
        TradeSignDTO lastCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 1);
        TradeSignDTO lastSecondCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 2);
        if (tempTradeSignList.get(0).getTradeSign() == null) {
            MAUtil.initTradeSignM5M20(tempTradeSignList);
        }
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(lastCandleDTO)) {
            return;
        }
        TradeSignDTO lastTradeSign = null;
        List<TradeSignDTO> tradeSigns = tempTradeSignList.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tradeSigns)) {
            lastTradeSign = tradeSigns.get(tradeSigns.size() - 1);
        }
        // 默认使用最新价格作为成交价格
        BigDecimal currTradePrice = lastCandleDTO.getClose();
        /**
         * 交易信号到来
         * 1. 上穿
         *   1.1 上次为sell
         */
        boolean tradeSignCome = false;
        String prefix = "";
        String tradeCode = "";
        // 上穿，设置此次信号为BUY
        if (lastSecondCalcCandleDTO.getMa5().compareTo(lastSecondCalcCandleDTO.getMa20()) <= 0
                && lastCalcCandleDTO.getMa5().compareTo(lastCalcCandleDTO.getMa20()) > 0) {
            lastCandleDTO.setTradeSign(TradeSignEnum.BUY_SIGN.getCode());
            lastCandleDTO.setTradePrice(currTradePrice);
            lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "buy";
        } else if (lastSecondCalcCandleDTO.getMa5().compareTo(lastSecondCalcCandleDTO.getMa20()) >= 0
                && lastCalcCandleDTO.getMa5().compareTo(lastCalcCandleDTO.getMa20()) < 0) {
            // 下穿，设置此次信号为SELL
            lastCandleDTO.setTradeSign(TradeSignEnum.SELL_SIGN.getCode());
            lastCandleDTO.setTradePrice(currTradePrice);
            lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "sell";
        } else {
            if (null != lastTradeSign) {
                BigDecimal slNum = DecimalUtil.toDecimal(lastTradeSign.getTradePrice()).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
                BigDecimal tpNum = slNum.multiply(BigDecimal.valueOf(7l));
//                BigDecimal slNum = stopLossNum;
//                BigDecimal tpNum = takeProfitNum;
                if (OkxCommon.checkIfMASellSign(lastTradeSign)) {
                    // 如果上一次是MA SELL
                    if ((DecimalUtil.toDecimal(currTradePrice).subtract(slNum)).compareTo(DecimalUtil.toDecimal(lastTradeSign.getTradePrice())) >= 0) {
                        // 如果当前交易价格-止损价格 > 上次MA信号SELL价。 到达止损
                        // 例如上次MA信号SELL价为80000，此时已经达到80800. 80800 - 700 = 80100 > 80000。 需要止损
                        lastCandleDTO.setTradeSign(TradeSignEnum.SL_BUY_SIGN.getCode());
                        lastCandleDTO.setTradePrice(currTradePrice);
                        lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "buy";
                    } else if ((DecimalUtil.toDecimal(currTradePrice).add(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradeSign.getTradePrice())) <= 0) {
                        // 如果当前交易价格+止盈价格 < 上次MA信号SELL价。 到达止盈
                        // 例如上次MA信号SELL价为80000，此时已经达到70000. 70000 + 6000 = 76000 < 80000。 需要止盈
                        lastCandleDTO.setTradeSign(TradeSignEnum.TP_BUY_SIGN.getCode());
                        lastCandleDTO.setTradePrice(currTradePrice);
                        lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "buy";
                    }
                } else if (OkxCommon.checkIfMABuySign(lastTradeSign)) {
                    // 如果上一次是MA BUY
                    if ((DecimalUtil.toDecimal(currTradePrice).add(slNum)).compareTo(DecimalUtil.toDecimal(lastTradeSign.getTradePrice())) <= 0) {
                        // 如果当前交易价格+止损价格 < 上次MA信号BUY价。 到达止损
                        // 例如上次MA信号BUY价为80800，此时已经达到80000. 80000 + 700 = 80700 < 80800。 需要止损
                        lastCandleDTO.setTradeSign(TradeSignEnum.SL_SELL_SIGN.getCode());
                        lastCandleDTO.setTradePrice(currTradePrice);
                        lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "sell";
                    } else if ((DecimalUtil.toDecimal(currTradePrice).subtract(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradeSign.getTradePrice())) >= 0) {
                        // 如果当前交易价格-止盈价格 > 上次MA信号BUY价。 到达止盈
                        // 例如上次MA信号BUY价为70000，此时已经达到80000. 80000 - 6000 = 74000 > 70000。 需要止盈
                        lastCandleDTO.setTradeSign(TradeSignEnum.TP_SELL_SIGN.getCode());
                        lastCandleDTO.setTradePrice(currTradePrice);
                        lastCandleDTO.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "sell";
                    }
                }
            } else {
                lastCandleDTO.setTradeSign(TradeSignEnum.NONE_SIGN.getCode());
            }
        }
        if (tradeSignCome) {
            log.info("{}交易信号来临。{}。交易时间:{},交易价格:{}", prefix, tradeCode, DateUtil.dateFormat(lastCandleDTO.getTradeTime()), DecimalUtil.format(lastCandleDTO.getTradePrice()));
        }
    }


}
