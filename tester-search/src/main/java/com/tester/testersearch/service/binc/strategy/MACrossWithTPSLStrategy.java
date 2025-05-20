package com.tester.testersearch.service.binc.strategy;

import com.alibaba.fastjson.JSON;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.BeanCopyUtil;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.CandleTradeSignalDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.service.CandleTradeSignalService;
import com.tester.testersearch.service.binc.binance.BinanceHelper;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import com.tester.testersearch.util.binc.tradeSign.MAUtil;
import com.tester.testersearch.util.binc.tradeSign.PrinterHelperV2;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private CandleTradeSignalService candleTradeSignalService;

    private static BigDecimal slTimes = new BigDecimal("0.01");
    private static BigDecimal tpTimes = new BigDecimal("0.07");


    public static Map<String, Map<Long, TradeSignDTO>> tradeSignMap = new HashMap();


    /**
     * @param startAt "20250401000000"
     * @throws BusinessException
     */
    public void runOnce(String startAt, String endAt, TradeParam tradeParam) throws BusinessException {
        MACrossWithTPSLStrategy.slTimes = tradeParam.getSlTimes();
        MACrossWithTPSLStrategy.tpTimes = tradeParam.getTpTimes();
        Map<Long, TradeSignDTO> tradeSignDTOMap = tradeSignMap.get(tradeParam.getBarEnum().getCode());
        if (CollectionUtils.isEmpty(tradeSignDTOMap)) {
            tradeSignDTOMap = new LinkedHashMap<>();
        }
        long maxId = Long.MAX_VALUE;
        if (!StringUtils.isEmpty(endAt)) {
            LocalDateTime localDateTime = DateUtil.getLocalDateTime(endAt);
            Date endDate = DateUtil.getDateFromLocalDateTime(localDateTime);
            maxId = endDate.getTime();
        }
        try {
            binanceHelper.traceLocal(startAt, 80, (ifLast) -> {
            }, true, tradeParam);
            Map<Long, TradeSignDTO> hisDataMap = BinanceHelper.getByBarEnumCode(tradeParam.getBarEnum().getCode());
            List<TradeSignDTO> tradeSignDTOS = hisDataMap.values().stream().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tradeSignDTOS)) {
                return;
            }
            AtomicBoolean ifLastAto = new AtomicBoolean(false);
            long lastUpdateTimestamp = 0L;
            int size = 0;
            StopWatch stopWatch = new StopWatch();
            do {
                List<TradeSignDTO> tradeList = tradeSignDTOS.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e.getTradeInfo()) || OkxCommon.checkIfHasTradeSign(e.getReverseTradeInfo())).collect(Collectors.toList());
                for (TradeSignDTO tradeSignDTO : tradeList) {
                    tradeSignDTOMap.put(tradeSignDTO.getId(), tradeSignDTO);
                }
                if (tradeSignDTOMap.size() != size) {
                    if (stopWatch.isRunning()) {
                        stopWatch.stop();
                    }
                    if(tradeParam.getNeedSave()) {
                        this.saveOrUpdateSignal(tradeParam, tradeSignDTOMap, size);
                    }
                    size = tradeSignDTOMap.size();
                    stopWatch.start("数据打印");
//                    PrinterHelper.printProfitsWithTPSL(tradeSignDTOMap.values().stream().collect(Collectors.toList()), tradeParam);
                    PrinterHelperV2.printProfitsWithTPSL(tradeSignDTOMap, tradeParam);
                    stopWatch.stop();
                    long lastTaskTimeMillis = stopWatch.getTotalTimeMillis();
                    System.out.println("本轮计算耗时:" + lastTaskTimeMillis);
                    stopWatch = new StopWatch();
                    stopWatch.start("数据处理");
                }
                TradeSignDTO tradeSignDTO = tradeSignDTOS.get(tradeSignDTOS.size() - 1);
                lastUpdateTimestamp = tradeSignDTO.getLastUpdateTimestamp();
                binanceHelper.traceLocal(startAt, 1, (ifLast) -> {
                    ifLastAto.set(ifLast);
                }, true, tradeParam);
                hisDataMap = BinanceHelper.getByBarEnumCode(tradeParam.getBarEnum().getCode());
                tradeSignDTOS = hisDataMap.values().stream().collect(Collectors.toList());
            } while (!ifLastAto.get() && lastUpdateTimestamp < maxId);
        } catch (Exception e) {
            log.error("测试异常。err:", e);
        }
    }

    private void saveOrUpdateSignal(TradeParam tradeParam, Map<Long, TradeSignDTO> tradeSignDTOMap, int size) {
        List<TradeSignDTO> tradeList = tradeSignDTOMap.values().stream().skip(size).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tradeList)) {
            return;
        }
        for (TradeSignDTO tradeSignDTO : tradeList) {
            CandleTradeSignalDomain signal = candleTradeSignalService.getByTimestamp(tradeParam, tradeSignDTO.getOpenTimestamp());
            TradeSignDTO.TradeInfo tradeInfo = tradeSignDTO.getTradeInfo();
            TradeSignDTO.TradeInfo pureMaTradeInfo = tradeSignDTO.getPureTradeInfo();
            if (null == signal) {
                BigDecimal fee = tradeParam.getTradeFee();
                if(OkxCommon.checkIfSLSign(tradeInfo)){
                    fee = tradeParam.getSlTradeFee();
                }
                CandleTradeSignalDomain saveDomain = new CandleTradeSignalDomain();
                saveDomain.init();
                saveDomain.setBKey(tradeParam.getBKey())
                        .setStrategyCode(tradeParam.getStrategyCode())
                        .setExtColumn(JSON.toJSONString(new CandleTradeSignalDomain.ExtColumn().setTradeParam(tradeParam)))
                        .setOpenTimestamp(tradeSignDTO.getOpenTimestamp())
                        .setTradeSign(tradeInfo.getTradeSign())
                        .setTradePrice(tradeInfo.getTradePrice())
                        .setTradeTime(tradeInfo.getTradeTime())
                        .setTradeSerialNum(tradeInfo.getTradeSerialNum())
                        .setTradeProfitsRate(DecimalUtil.toDecimal(tradeInfo.getTradeProfitsRate()).subtract(fee))
                        .setTradeProfits(tradeInfo.getTradeProfits())
                        .setTradeEnd(tradeInfo.getTradeEnd())
                        .setTradeStart(tradeInfo.getTradeStart())
                        .setMaTradeProfitsRate(pureMaTradeInfo != null ? pureMaTradeInfo.getTradeProfitsRate() : null)
                        .setMaTradeProfits(pureMaTradeInfo != null ? pureMaTradeInfo.getTradeProfits() : null)
                        .setSkipNum(null)
                        .setActualTrade(0)
                        .setExtColumn(null);
                candleTradeSignalService.save(saveDomain);
            }
        }
    }


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
            // 下穿，设置此次信号为SELL
            tradeSignCome = true;
            prefix = "MA";
            tradeCode = "sell";
            setAndFillTradeInfo(lastCandleDTO, currTradePrice, lastTradeCandle, TradeSignEnum.SELL_SIGN, lastMaTradeCandle);
        } else {
            if (null != lastTradeCandle) {
                BigDecimal lastTradePrice = lastTradeCandle.getTradeInfo().getTradePrice();
                // 止损价格差额
                BigDecimal slNum = DecimalUtil.toDecimal(lastTradePrice).multiply(slTimes);
                // 止盈价格差额
                BigDecimal tpNum = DecimalUtil.toDecimal(lastTradePrice).multiply(tpTimes);
                if (OkxCommon.checkIfMASellSign(lastTradeCandle.getTradeInfo())) {
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
                } else if (OkxCommon.checkIfMABuySign(lastTradeCandle.getTradeInfo())) {
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

    private static void setAndFillTradeInfoForTpsl(TradeSignDTO lastCandleDTO, BigDecimal tpslNum, TradeSignDTO lastTradeCandle, TradeSignEnum signEnum) throws BusinessException {
        TradeSignDTO.TradeInfo tradeInfo = new TradeSignDTO.TradeInfo();
        lastCandleDTO.setTradeInfo(tradeInfo);
        tradeInfo.setTradeSign(signEnum.getCode());
        tradeInfo.setTradeTime(new Date(lastCandleDTO.getLastUpdateTimestamp()));
        BigDecimal lastTradePrice = lastTradeCandle.getTradeInfo().getTradePrice();
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
