package com.tester.testersearch.service.binc.strategy;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.BeanCopyUtil;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ma加上止盈止损
 * TP (Take Profit) 和 SL (Stop Loss)
 */
@Service
@Slf4j
public class MACrossWithTPSLStrategy2 {
    @Autowired
    protected BinanceHelper binanceHelper;


    /**
     * 计算并设置MA交易信号
     *
     * @param tradeSignList
     * @throws BusinessException
     */
    public static void calculateTradeSign_excludeLast(List<TradeSignDTO> tradeSignList, TradeParam tradeParam, boolean first) throws BusinessException {
        TradeSignDTO lastCandleDTO = tradeSignList.get(tradeSignList.size() - 1);
        List<TradeSignDTO> tempTradeSignList = tradeSignList.stream().limit(tradeSignList.size() - 1).collect(Collectors.toList());
        TradeSignDTO lastCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 1);
        TradeSignDTO lastSecondCalcCandleDTO = tempTradeSignList.get(tempTradeSignList.size() - 2);
        if (first) {
            MAUtil.initTradeSignM5M20(tempTradeSignList);
        }
        // 最后一次交易蜡烛
        TradeSignDTO lastTradeCandle = tempTradeSignList.stream()
                .filter(e -> OkxCommon.checkIfHasTradeSign(e.getTradeInfo()))
                .reduce((a, b) -> b)
                .orElse(null);
        // 最后一次交易蜡烛
        TradeSignDTO lastMaTradeCandle = tempTradeSignList.stream()
                .filter(e -> OkxCommon.checkIfHasMATradeSign(e.getTradeInfo()))
                .reduce((a, b) -> b)
                .orElse(null);
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(lastCandleDTO.getTradeInfo())) {
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
            if (lastCandleDTO.getMa5().compareTo(lastCandleDTO.getMa20()) < 0
                    || OkxCommon.checkIfMABuySign(lastTradeCandle.getTradeInfo())) {
                // 应该上穿，但是此时是ma5小于ma20，直接return
                // 或者上一次已经是buy
                return;
            }
            if(null == lastCandleDTO.getTryTradePrice()){
                // 信号出现，模拟限价下单-buy
                lastCandleDTO.setTryTradePrice(lastCandleDTO.getClose().subtract(lastCandleDTO.getClose().multiply(new BigDecimal("0.00001"))));
                lastCandleDTO.setTryTradeTime(lastCandleDTO.getLastUpdateTimestamp());
                return;
            }else if(lastCandleDTO.getClose().compareTo(lastCandleDTO.getTryTradePrice()) > 0){
                // 如果现价高于下单价，无法成交。等待N秒后重新下限价单
                if(lastCandleDTO.getLastUpdateTimestamp()-lastCandleDTO.getTryTradeTime() > tradeParam.getRetryTradeGap()){
                    // 重新下单
                    lastCandleDTO.setTryTradePrice(lastCandleDTO.getClose().subtract(lastCandleDTO.getClose().multiply(new BigDecimal("0.00001"))));
                    lastCandleDTO.setTryTradeTime(lastCandleDTO.getLastUpdateTimestamp());
                }
                return;
            }
            // 上穿，设置此次信号为BUY
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "buy";
            setAndFillTradeInfo(lastCandleDTO, currTradePrice, lastTradeCandle, TradeSignEnum.BUY_SIGN, lastMaTradeCandle);
        } else if (lastSecondCalcCandleDTO.getMa5().compareTo(lastSecondCalcCandleDTO.getMa20()) >= 0
                && lastCalcCandleDTO.getMa5().compareTo(lastCalcCandleDTO.getMa20()) < 0) {
            if (lastCandleDTO.getMa5().compareTo(lastCandleDTO.getMa20()) > 0
                    || OkxCommon.checkIfMASellSign(lastTradeCandle.getTradeInfo())) {
                // 应该下穿，但是此时是ma5大于ma20，直接return
                // 或者上一次已经是sell
                return;
            }
            if(null == lastCandleDTO.getTryTradePrice()){
                // 信号出现，模拟限价下单-sell
                lastCandleDTO.setTryTradePrice(lastCandleDTO.getClose().add(lastCandleDTO.getClose().multiply(new BigDecimal("0.00001"))));
                lastCandleDTO.setTryTradeTime(lastCandleDTO.getLastUpdateTimestamp());
                return;
            }else if(lastCandleDTO.getClose().compareTo(lastCandleDTO.getTryTradePrice()) < 0){
                // 如果现价低于下单价，无法成交。等待N秒后重新下限价单
                if(lastCandleDTO.getLastUpdateTimestamp()-lastCandleDTO.getTryTradeTime() > tradeParam.getRetryTradeGap()){
                    // 重新下单
                    lastCandleDTO.setTryTradePrice(lastCandleDTO.getClose().add(lastCandleDTO.getClose().multiply(new BigDecimal("0.00001"))));
                    lastCandleDTO.setTryTradeTime(lastCandleDTO.getLastUpdateTimestamp());
                }
                return;
            }
            // 下穿，设置此次信号为SELL
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "sell";
            setAndFillTradeInfo(lastCandleDTO, currTradePrice, lastTradeCandle, TradeSignEnum.SELL_SIGN, lastMaTradeCandle);
        } else {
            if (null != lastTradeCandle) {
                BigDecimal lastTradePrice = lastTradeCandle.getTradeInfo().getTradePrice();
                // 止损价格差额
                BigDecimal slNum = DecimalUtil.toDecimal(lastTradePrice).multiply(tradeParam.getSlTimes());
                // 止盈价格差额
                BigDecimal tpNum = DecimalUtil.toDecimal(lastTradePrice).multiply(tradeParam.getTpTimes());
                if (OkxCommon.checkIfMASellSign(lastTradeCandle.getTradeInfo())) {
                    // 如果上一次是MA SELL
                    if ((DecimalUtil.toDecimal(currHighPrice).subtract(slNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) >= 0) {
                        // 如果当前交易价格-止损价格 > 上次MA信号SELL价。 到达止损
                        // 例如上次MA信号SELL价为80000，此时已经达到80800. 80800 - 700 = 80100 > 80000。 需要止损，且止损价为80100
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "buy";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, slNum, lastTradeCandle, TradeSignEnum.SL_BUY_SIGN,tradeParam);
                    } else if ((DecimalUtil.toDecimal(currLowPrice).add(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) <= 0) {
                        // 如果当前交易价格+止盈价格 < 上次MA信号SELL价。 到达止盈
                        // 例如上次MA信号SELL价为80000，此时已经达到70000. 70000 + 6000 = 76000 < 80000。 需要止盈，且止损价为76000
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "buy";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, tpNum, lastTradeCandle, TradeSignEnum.TP_BUY_SIGN,tradeParam);
                    }
                } else if (OkxCommon.checkIfMABuySign(lastTradeCandle.getTradeInfo())) {
                    // 如果上一次是MA BUY
                    if ((DecimalUtil.toDecimal(currLowPrice).add(slNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) <= 0) {
                        // 如果当前交易价格+止损价格 < 上次MA信号BUY价。 到达止损
                        // 例如上次MA信号BUY价为80800，此时已经达到80000. 80000 + 700 = 80700 < 80800。 需要止损
                        tradeSignCome = true;
                        prefix = "止损";
                        tradeCode = "sell";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, slNum, lastTradeCandle, TradeSignEnum.SL_SELL_SIGN,tradeParam);
                    } else if ((DecimalUtil.toDecimal(currHighPrice).subtract(tpNum)).compareTo(DecimalUtil.toDecimal(lastTradePrice)) >= 0) {
                        // 如果当前交易价格-止盈价格 > 上次MA信号BUY价。 到达止盈
                        // 例如上次MA信号BUY价为70000，此时已经达到80000. 80000 - 6000 = 74000 > 70000。 需要止盈
                        tradeSignCome = true;
                        prefix = "止盈";
                        tradeCode = "sell";
                        setAndFillTradeInfoForTpsl(lastCandleDTO, tpNum, lastTradeCandle, TradeSignEnum.TP_SELL_SIGN,tradeParam);
                    }
                }
            }
        }
        if (tradeSignCome) {
            log.info("{}交易信号来临。{}。交易时间:{},交易价格:{}", prefix, tradeCode, DateUtil.dateFormat(lastCandleDTO.getTradeInfo().getTradeTime()), DecimalUtil.format(lastCandleDTO.getTradeInfo().getTradePrice()));
        }
    }

    private static void setAndFillTradeInfo(TradeSignDTO lastCandleDTO,
                                            BigDecimal currTradePrice,
                                            TradeSignDTO lastTradeCandle,
                                            TradeSignEnum signEnum,
                                            TradeSignDTO lastMaTradeCandle) {
        TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
        lastCandleDTO.setTradeInfo(tradeInfo);
        lastCandleDTO.setPureTradeInfo(tradeInfo);
        tradeInfo.setTradeSign(signEnum.getCode());
        tradeInfo.setTradePrice(currTradePrice);
        tradeInfo.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
        tradeInfo.setTradeStart(ConstantList.ONE);
        tradeInfo.setTradeEnd(ConstantList.ONE);
        if (null != lastTradeCandle) {
            tradeInfo.setTradeSerialNum(lastTradeCandle.getTradeInfo().getTradeSerialNum() + 1);
            if (OkxCommon.checkIfHasTPLSTradeSign(lastTradeCandle.getTradeInfo())) {
                // 如果上一次是止盈止损，这一次不计算，记为非交易终点
                tradeInfo.setTradeEnd(ConstantList.ZERO);
                tradeInfo.setTradeProfitsRate(BigDecimal.ZERO);
                tradeInfo.setTradeProfits(BigDecimal.ZERO);
                if (null != lastMaTradeCandle) {
                    TradeSignDTO.TradeInfo pureTradeInfo = new TradeSignDTO.TradeInfo();
                    BeanCopyUtil.copyPropertiesIgnoreNull(tradeInfo, pureTradeInfo);
                    BigDecimal lastTradePrice = lastMaTradeCandle.getTradeInfo().getTradePrice();
                    BigDecimal profits = calculateProfits(lastTradePrice, currTradePrice, signEnum);
                    BigDecimal tradeProfitRate = profits.divide(lastTradePrice, 4, BigDecimal.ROUND_HALF_UP);
                    pureTradeInfo.setTradeEnd(ConstantList.ONE);
                    pureTradeInfo.setTradeProfitsRate(tradeProfitRate);
                    pureTradeInfo.setTradeProfits(profits);
                    if (!Objects.equals(lastMaTradeCandle.getTradeInfo().getTradeSign(), signEnum.getCode())) {
                        tradeInfo.setTradeProfits(profits);
                        tradeInfo.setTradeProfits(profits);
                    }
                    lastCandleDTO.setPureTradeInfo(pureTradeInfo);
                }
            } else {
                // 当前为buy，上一次就是sell。 用上一次的减这一次的，计算盈利率
                // 当前为sell，上一次就是buy。 用这一次的减上一次的，计算盈利率
                BigDecimal lastTradePrice = lastTradeCandle.getTradeInfo().getTradePrice();
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

    private static void setAndFillTradeInfoForTpsl(TradeSignDTO lastCandleDTO, BigDecimal tpslNum, TradeSignDTO lastTradeCandle, TradeSignEnum signEnum, TradeParam tradeParam) throws BusinessException {
        TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
        lastCandleDTO.setTradeInfo(tradeInfo);
        tradeInfo.setTradeSign(signEnum.getCode());
        tradeInfo.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
        BigDecimal lastTradePrice = lastTradeCandle.getTradeInfo().getTradePrice();
        switch (signEnum) {
            case SL_BUY_SIGN:
                // 止损buy，说明上一次是sell，且已经突破sell价+tpslNum
                tradeInfo.setTradePrice(lastTradePrice.add(tpslNum));
                tradeInfo.setTradeProfitsRate(tradeParam.getSlTimes().negate());
                tradeInfo.setTradeProfits(tpslNum.negate());
                break;
            case TP_BUY_SIGN:
                // 止盈buy，说明上一次是sell，且已经跌破sell价-tpslNum
                tradeInfo.setTradePrice(lastTradePrice.subtract(tpslNum));
                tradeInfo.setTradeProfitsRate(tradeParam.getTpTimes());
                tradeInfo.setTradeProfits(tpslNum);
                break;
            case SL_SELL_SIGN:
                // 止损sell，说明上一次是buy，且已经跌破buy价-tpslNum
                tradeInfo.setTradePrice(lastTradePrice.subtract(tpslNum));
                tradeInfo.setTradeProfitsRate(tradeParam.getSlTimes().negate());
                tradeInfo.setTradeProfits(tpslNum.negate());
                break;
            case TP_SELL_SIGN:
                // 止盈sell，说明上一次是buy，且已经突破buy价+tpslNum
                tradeInfo.setTradePrice(lastTradePrice.add(tpslNum));
                tradeInfo.setTradeProfitsRate(tradeParam.getTpTimes());
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
        tradeInfo.setTradeSerialNum(lastTradeCandle.getTradeInfo().getTradeSerialNum());
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
