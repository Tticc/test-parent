package com.tester.testersearch.service.binc.strategy;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ma加上止盈止损。反向操作
 * TP (Take Profit) 和 SL (Stop Loss)
 */
@Service
@Slf4j
public class MACrossWithTPSLStrategy_reverse {

    private static BigDecimal slTimes = new BigDecimal("0.01");
    private static BigDecimal tpTimes = new BigDecimal("0.07");


    /**
     * 计算并设置MA交易信号
     *
     * @param tradeSignList
     * @throws BusinessException
     */
    public static void calculateTradeSign_excludeLast_reverse(List<TradeSignDTO> tradeSignList, TradeParam tradeParam, boolean first) throws BusinessException {
        MACrossWithTPSLStrategy_reverse.slTimes = tradeParam.getReverseSlTimes();
        MACrossWithTPSLStrategy_reverse.tpTimes = tradeParam.getReverseTpTimes();
        TradeSignDTO lastCandleDTO = tradeSignList.get(tradeSignList.size() - 1);
        List<TradeSignDTO> tempTradeSignList = tradeSignList.stream().limit(tradeSignList.size() - 1).collect(Collectors.toList());
        TradeSignDTO lastCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 1);
        TradeSignDTO lastSecondCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 2);
        if (first) {
            MAUtil.initTradeSignM5M20(tempTradeSignList);
        }
        // 最后一次交易蜡烛
        TradeSignDTO lastTradeCandle = tempTradeSignList.stream()
                .filter(e -> OkxCommon.checkIfHasTradeSign(e.getReverseTradeInfo()))
                .reduce((a, b) -> b)
                .orElse(null);
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(lastCandleDTO.getReverseTradeInfo())) {
            return;
        }
        // 默认使用最新价格作为成交价格
        BigDecimal currTradePrice = lastCandleDTO.getClose();
        BigDecimal currLowPrice = lastCandleDTO.getLow();
        BigDecimal currHighPrice = lastCandleDTO.getHigh();
        /**
         * 交易信号到来
         * 1. 上穿
         *   1.1 上次为sell
         */
        boolean tradeSignCome = false;
        String prefix = "";
        String tradeCode = "";
        if (lastSecondCalcCandleDTO.getMa5().compareTo(lastSecondCalcCandleDTO.getMa20()) <= 0
                && lastCalcCandleDTO.getMa5().compareTo(lastCalcCandleDTO.getMa20()) > 0) {
            // 上穿，但是reverse，设置此次信号为SELL
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "sell";
            setAndFillTradeInfo(lastCandleDTO, currTradePrice, lastTradeCandle, TradeSignEnum.SELL_SIGN);
        } else if (lastSecondCalcCandleDTO.getMa5().compareTo(lastSecondCalcCandleDTO.getMa20()) >= 0
                && lastCalcCandleDTO.getMa5().compareTo(lastCalcCandleDTO.getMa20()) < 0) {
            // 下穿，但是reverse，设置此次信号为BUY
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "buy";
            setAndFillTradeInfo(lastCandleDTO, currTradePrice, lastTradeCandle, TradeSignEnum.BUY_SIGN);
        } else {
            if (null != lastTradeCandle) {
                BigDecimal lastTradePrice = lastTradeCandle.getReverseTradeInfo().getTradePrice();
                // 止损价格差额
                BigDecimal slNum = DecimalUtil.toDecimal(lastTradePrice).multiply(slTimes);
                // 止盈价格差额
                BigDecimal tpNum = DecimalUtil.toDecimal(lastTradePrice).multiply(tpTimes);
                if (OkxCommon.checkIfMASellSign(lastTradeCandle.getReverseTradeInfo())) {
                    // 如果上一次是MA SELL
                    if ((DecimalUtil.toDecimal(currHighPrice).subtract(slNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) >= 0) {
                        // 如果当前交易价格-止损价格 > 上次MA信号SELL价。 到达止损
                        // 例如上次MA信号SELL价为80000，此时已经达到80800. 80800 - 700 = 80100 > 80000。 需要止损，且止损价为80100
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "buy";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, slNum, lastTradeCandle, TradeSignEnum.SL_BUY_SIGN);
                    } else if ((DecimalUtil.toDecimal(currLowPrice).add(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) <= 0) {
                        // 如果当前交易价格+止盈价格 < 上次MA信号SELL价。 到达止盈
                        // 例如上次MA信号SELL价为80000，此时已经达到70000. 70000 + 6000 = 76000 < 80000。 需要止盈，且止损价为76000
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "buy";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, tpNum, lastTradeCandle, TradeSignEnum.TP_BUY_SIGN);
                    }
                } else if (OkxCommon.checkIfMABuySign(lastTradeCandle.getReverseTradeInfo())) {
                    // 如果上一次是MA BUY
                    if ((DecimalUtil.toDecimal(currLowPrice).add(slNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) <= 0) {
                        // 如果当前交易价格+止损价格 < 上次MA信号BUY价。 到达止损
                        // 例如上次MA信号BUY价为80800，此时已经达到80000. 80000 + 700 = 80700 < 80800。 需要止损
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "sell";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, slNum, lastTradeCandle, TradeSignEnum.SL_SELL_SIGN);
                    } else if ((DecimalUtil.toDecimal(currHighPrice).subtract(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) >= 0) {
                        // 如果当前交易价格-止盈价格 > 上次MA信号BUY价。 到达止盈
                        // 例如上次MA信号BUY价为70000，此时已经达到80000. 80000 - 6000 = 74000 > 70000。 需要止盈
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "sell";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, tpNum, lastTradeCandle, TradeSignEnum.TP_SELL_SIGN);
                    }
                }
            }
        }
        if (tradeSignCome) {
//            log.info("reverse-{}交易信号来临。{}。交易时间:{},交易价格:{}", prefix, tradeCode, DateUtil.dateFormat(lastCandleDTO.getReverseTradeInfo().getTradeTime()), DecimalUtil.format(lastCandleDTO.getReverseTradeInfo().getTradePrice()));
        }
    }

    private static void setAndFillTradeInfo(TradeSignDTO lastCandleDTO, BigDecimal currTradePrice, TradeSignDTO lastTradeCandle, TradeSignEnum signEnum) {
        TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
        lastCandleDTO.setReverseTradeInfo(tradeInfo);
        tradeInfo.setTradeSign(signEnum.getCode());
        tradeInfo.setTradePrice(currTradePrice);
        tradeInfo.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
        tradeInfo.setTradeStart(ConstantList.ONE);
        tradeInfo.setTradeEnd(ConstantList.ONE);
        if (null != lastTradeCandle) {
            tradeInfo.setTradeSerialNum(lastTradeCandle.getReverseTradeInfo().getTradeSerialNum() + 1);
            if (OkxCommon.checkIfHasTPLSTradeSign(lastTradeCandle.getReverseTradeInfo())) {
                // 如果上一次是止盈止损，这一次不计算，记为非交易终点
                tradeInfo.setTradeEnd(ConstantList.ZERO);
                tradeInfo.setTradeProfitsRate(BigDecimal.ZERO);
                tradeInfo.setTradeProfits(BigDecimal.ZERO);
            } else {
                // 当前为buy，上一次就是sell。 用上一次的减这一次的，计算盈利率
                // 当前为sell，上一次就是buy。 用这一次的减上一次的，计算盈利率
                BigDecimal lastTradePrice = lastTradeCandle.getReverseTradeInfo().getTradePrice();
                BigDecimal profits = calculateProfits(lastTradePrice, currTradePrice, signEnum);
                BigDecimal tradeProfitRate = profits.divide(lastTradePrice, 4, BigDecimal.ROUND_HALF_UP);
                tradeInfo.setTradeProfitsRate(tradeProfitRate);
                tradeInfo.setTradeProfits(profits);
            }
        } else {
            tradeInfo.setTradeSerialNum(ConstantList.ONE);
            tradeInfo.setTradeProfitsRate(BigDecimal.ZERO);
            tradeInfo.setTradeProfits(BigDecimal.ZERO);
            tradeInfo.setTradeEnd(ConstantList.ZERO);
        }
    }

    private static void setAndFillTradeInfoForTpsl(TradeSignDTO lastCandleDTO, BigDecimal tpslNum, TradeSignDTO lastTradeCandle, TradeSignEnum signEnum) throws BusinessException {
        TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
        lastCandleDTO.setReverseTradeInfo(tradeInfo);
        tradeInfo.setTradeSign(signEnum.getCode());
        tradeInfo.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
        BigDecimal lastTradePrice = lastTradeCandle.getReverseTradeInfo().getTradePrice();
        switch (signEnum) {
            case SL_BUY_SIGN:
                // 止损buy，说明上一次是sell，且已经突破sell价+tpslNum
                tradeInfo.setTradePrice(lastTradePrice.add(tpslNum));
                tradeInfo.setTradeProfitsRate(slTimes.negate());
                tradeInfo.setTradeProfits(tpslNum.negate());
                break;
            case TP_BUY_SIGN:
                // 止盈buy，说明上一次是sell，且已经跌破sell价-tpslNum
                tradeInfo.setTradePrice(lastTradePrice.subtract(tpslNum));
                tradeInfo.setTradeProfitsRate(tpTimes);
                tradeInfo.setTradeProfits(tpslNum);
                break;
            case SL_SELL_SIGN:
                // 止损sell，说明上一次是buy，且已经跌破buy价-tpslNum
                tradeInfo.setTradePrice(lastTradePrice.subtract(tpslNum));
                tradeInfo.setTradeProfitsRate(slTimes.negate());
                tradeInfo.setTradeProfits(tpslNum.negate());
                break;
            case TP_SELL_SIGN:
                // 止盈sell，说明上一次是buy，且已经突破buy价+tpslNum
                tradeInfo.setTradePrice(lastTradePrice.add(tpslNum));
                tradeInfo.setTradeProfitsRate(tpTimes);
                tradeInfo.setTradeProfits(tpslNum);
                break;
            case BUY_SIGN:
            case SELL_SIGN:
            case NONE_SIGN:
            default:
                throw new BusinessException(5000L, "信号错误");
        }
        tradeInfo.setTradeStart(ConstantList.ZERO);
        tradeInfo.setTradeEnd(ConstantList.ONE);
        tradeInfo.setTradeSerialNum(lastTradeCandle.getReverseTradeInfo().getTradeSerialNum() + 1);
    }

    private static BigDecimal calculateProfits(BigDecimal lastTradePrice, BigDecimal currTradePrice, TradeSignEnum signEnum) {
        BigDecimal profits = BigDecimal.ZERO;
        switch (signEnum) {
            case BUY_SIGN:
            case SL_BUY_SIGN:
            case TP_BUY_SIGN:
                profits = lastTradePrice.subtract(currTradePrice);
                break;
            case SELL_SIGN:
            case SL_SELL_SIGN:
            case TP_SELL_SIGN:
                profits = currTradePrice.subtract(lastTradePrice);
                break;
            case NONE_SIGN:
            default:
                break;
        }
        return profits;
    }
}
