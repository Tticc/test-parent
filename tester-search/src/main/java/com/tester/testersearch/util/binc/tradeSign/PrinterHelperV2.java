package com.tester.testersearch.util.binc.tradeSign;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.binc.okx.OkxCommon;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PrinterHelperV2 {


    public static void printProfitsWithTPSL(Map<Long, TradeSignDTO> tradeMap, TradeParam tradeParam) {
        List<TradeSignDTO> tradeList = tradeMap.values().stream().collect(Collectors.toList());

        // 初始化
//        Integer reverseSkipNum = tradeParam.getReverseSkipNum();
//        Integer reverseTakeNum = tradeParam.getReverseTakeNum();
        Integer reverseSkipNum = -1;
        Integer reverseTakeNum = -1;

        List<Integer> resetSkipRecords = new ArrayList<>();
        List<Integer> pureMaList = new ArrayList<>();
        List<Integer> profitsList = new ArrayList<>();
        List<BigDecimal> profitsRates = new ArrayList<>();
        List<Integer> tradeFeeList = new ArrayList<>();
        // 默认倍率
        double defaultTimes = 1.0d;
        // 默认倍率提升率。当一次交易为负数，下次提升交易数额，数额由此交易倍率决定。
        // 例如：默认数额为100，multiTimes=1.2。
        // 那么当一次交易收益为负数之后，下次交易数额为100*1.2。仍然收益为负数，下下次交易数额为100*1.2*1.2
        // 当某一次收益为正multiTimes重置为1.0
        double multiTimes = 1.0d;

        // 跳过的横盘交易次数。设置为0代表不会跳过
        // 当收益超过skipNumber时，跳过未来几次的交易信号。因为每次大收益之后，都会进入横盘，因此跳过几次交易，避免部分横盘交易
        int defaultSkipNumber = 1000;
        AtomicReference<TradeSignDTO> lastMa = new AtomicReference<>(null);
        AtomicInteger skip = new AtomicInteger(0);
        boolean normalTrade = true;
        for (TradeSignDTO value : tradeList) {
            int currentSkip = skip.get();
            resetSkipByProfit(value,currentSkip,skip,tradeParam,pureMaList, resetSkipRecords);
//            resetSkipByMa(lastMa,value,currentSkip,skip,tradeParam,pureMaList, resetSkipRecords);
            if (normalTrade) {
                TradeSignDTO.TradeInfo tradeInfo = value.getTradeInfo();
                if (currentSkip <= 0) {
                    value.setActualTradeInfo(tradeInfo);
                }
                // 正向交易
                if (null == tradeInfo || !Objects.equals(tradeInfo.getTradeEnd(), ConstantList.ONE)) {
                    continue;
                }
                int profits = tradeInfo.getTradeProfits().intValue();
                BigDecimal profitRate = tradeInfo.getTradeProfitsRate();
                boolean hitHuge = tradeInfo.getTradeProfitsRate().compareTo(tradeParam.getSkipTimes()) >= 0;
                if (currentSkip > 0) {
                    if (hitHuge && tradeParam.getKeepSkipAfterHuge() > currentSkip && tradeParam.getKeepSkipAfterHuge() > 0) {
//                        skip.set(tradeParam.getKeepSkipAfterHuge());
                    }
                    continue;
                }
                profitsList.add(profits);
                profitsRates.add(profitRate.subtract(new BigDecimal("0.0004")));
                BigDecimal feeBig = DecimalUtil.toDecimal(tradeInfo.getTradePrice()).multiply(new BigDecimal("0.0004"));
                tradeFeeList.add(feeBig.intValue());

                // 如果达到大额收益，开始进入反向交易
                if (hitHuge) {
//                    skip.set(tradeParam.getSkipAfterHuge());
                    if(tradeParam.getReverseSkipNum() > 0 || tradeParam.getReverseTakeNum() > 0) {
                        normalTrade = false;
                        reverseSkipNum = tradeParam.getReverseSkipNum();
                        reverseTakeNum = tradeParam.getReverseTakeNum();
                        if (OkxCommon.checkIfHasTPLSTradeSign(tradeInfo)) {
                            reverseSkipNum += 1;
                        }
                    }
                }
            } else {
                TradeSignDTO.TradeInfo reverseTradeInfo = value.getReverseTradeInfo();
                if(reverseSkipNum <= 0 && reverseTakeNum > 0){
                    value.setActualTradeInfo(reverseTradeInfo);
                }
                // 逆向交易
                if (null != reverseTradeInfo) {
                    if (reverseSkipNum > 0 && OkxCommon.checkIfHasMATradeSign(reverseTradeInfo)) {
                        reverseSkipNum -= 1;
                    } else if(reverseSkipNum <= 0 && Objects.equals(reverseTradeInfo.getTradeEnd(), ConstantList.ONE)){
                        reverseTakeNum -= 1;
                        if (reverseTakeNum >= 0) {
                            profitsList.add(reverseTradeInfo.getTradeProfits().intValue());
                            profitsRates.add(reverseTradeInfo.getTradeProfitsRate().subtract(new BigDecimal("0.0004")));
                            BigDecimal feeBig = DecimalUtil.toDecimal(reverseTradeInfo.getTradePrice()).multiply(new BigDecimal("0.0004"));
                            tradeFeeList.add(feeBig.intValue());
                        } else {
                            normalTrade = true;
                        }
                    }
                }
                TradeSignDTO.TradeInfo tradeInfo = value.getTradeInfo();
                if (tradeInfo != null && Objects.equals(tradeInfo.getTradeEnd(), ConstantList.ONE)) {
                    if(tradeInfo.getTradeProfitsRate().compareTo(tradeParam.getSkipTimes()) >= 0) {
                        if (OkxCommon.checkIfHasMATradeSign(tradeInfo)) {
                            reverseSkipNum = tradeParam.getReverseSkipNum();
                            reverseTakeNum = tradeParam.getReverseTakeNum();
                        } else if (OkxCommon.checkIfHasTPLSTradeSign(tradeInfo)) {
                            // 如果是止盈止损交易信号
                            reverseSkipNum = tradeParam.getReverseSkipNum() + 1;
                            reverseTakeNum = tradeParam.getReverseTakeNum();
                        }
                        normalTrade = false;
                        if (currentSkip > 0) {
                            int leftSkip = currentSkip;
                            if (tradeParam.getKeepSkipAfterHuge() > leftSkip && tradeParam.getKeepSkipAfterHuge() > 0) {
//                                System.out.println("reverse剩余skip："+leftSkip+",重置skip为："+tradeParam.getKeepSkipAfterHuge());
//                                skip.set(tradeParam.getKeepSkipAfterHuge());
                            }
                        }

                    }
                }
            }

        }

        if(profitsList.size() >= 10){
            System.out.println("normalTrade = " + normalTrade);
        }

        List<BigDecimal> maChangeList = tradeList.stream()
                .filter(e -> e.getTradeInfo() != null && Objects.equals(e.getTradeInfo().getTradeEnd(), ConstantList.ONE))
                .map(e -> e.getTradeInfo().getTradeProfitsRate())
                .collect(Collectors.toList());
        List<Integer> reverseMaChangeList = tradeList.stream()
                .filter(e -> e.getReverseTradeInfo() != null && Objects.equals(e.getReverseTradeInfo().getTradeEnd(), ConstantList.ONE))
                .map(e -> e.getReverseTradeInfo().getTradeProfits().intValue())
                .collect(Collectors.toList());
        Integer sum = profitsList.stream().mapToInt(Integer::intValue).sum();
        Integer feeSum = tradeFeeList.stream().mapToInt(Integer::intValue).sum();
        BigDecimal rateSum = profitsRates.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        Collections.reverse(pureMaList);
        Collections.reverse(maChangeList);
        Collections.reverse(reverseMaChangeList);
        Collections.reverse(profitsList);
        Collections.reverse(profitsRates);
        Collections.reverse(resetSkipRecords);
        System.out.println("skip:" + skip.get());
        System.out.println("rese.size():" + resetSkipRecords.size() + ", resetSkipRecords = " + resetSkipRecords);
        System.out.println("pureMaList.size():" + pureMaList.size() + ", pureMaList = " + pureMaList);
        System.out.println("maChange.size():" + maChangeList.size() + ", maChangeList = " + maChangeList);
        System.out.println("profitList.size():" + profitsList.size() + ", profitList = " + profitsList);
        System.out.println("profits.size() :" + profitsRates.size() + ", profitsRates = " + profitsRates);
        System.out.println("sum = " + sum);
        System.out.println("rate sum = " + rateSum);
        System.out.println("累计盈利:" + (sum - feeSum) + ". 交易数(买+卖一次记1)：" + profitsList.size() + ", 交易费总计：" + feeSum);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void resetSkipByProfit(TradeSignDTO value,
                                          int currentSkip,
                                          AtomicInteger skip,
                                          TradeParam tradeParam,
                                          List<Integer> pureMaList,
                                          List<Integer> resetSkipRecords){
        TradeSignDTO.TradeInfo tradeInfo = value.getTradeInfo();
        if(tradeInfo == null || !Objects.equals(tradeInfo.getTradeEnd(), ConstantList.ONE)){
            return;
        }
        int i = skip.decrementAndGet();
        if (tradeInfo.getTradeProfitsRate().compareTo(tradeParam.getSkipTimes()) >= 0) {
            if (currentSkip > 0) {
                if (tradeParam.getKeepSkipAfterHuge() >= currentSkip && tradeParam.getKeepSkipAfterHuge() > 0) {
                    skip.set(tradeParam.getKeepSkipAfterHuge());
                    resetSkipRecords.add(currentSkip);
                    System.out.println("ma超过大收益收益率，当前：" + currentSkip + "，重置为：" + tradeParam.getSkipAfterHuge() + "，收益：" + tradeInfo.getTradeProfits() + "，交易时间:" + DateUtil.dateFormat(value.getTradeInfo().getTradeTime()));
                }
            } else {
                skip.set(tradeParam.getSkipAfterHuge());
                resetSkipRecords.add(currentSkip);
                System.out.println("ma超过大收益收益率，当前：" + currentSkip + "，重置为：" + tradeParam.getSkipAfterHuge() + "，收益：" + tradeInfo.getTradeProfits() + "，交易时间:" + DateUtil.dateFormat(value.getTradeInfo().getTradeTime()));
            }
        }

    }

    private static void resetSkipByMa(AtomicReference<TradeSignDTO> lastMa,
                                      TradeSignDTO value,
                                      int currentSkip,
                                      AtomicInteger skip,
                                      TradeParam tradeParam,
                                      List<Integer> pureMaList,
                                      List<Integer> resetSkipRecords) {
        // 仅使用MA计算hugeProfit
        if (OkxCommon.checkIfHasMATradeSign(value.getTradeInfo())) {
            skip.decrementAndGet();
            if (lastMa.get() != null) {
                TradeSignDTO.TradeInfo lastMaTradeInfo = lastMa.get().getTradeInfo();
                TradeSignDTO.TradeInfo currMaTradeInfo = value.getTradeInfo();
                BigDecimal profits;
                if (OkxCommon.checkIfMABuySign(lastMaTradeInfo)) {
                    // 如果上一次是buy
                    profits = currMaTradeInfo.getTradePrice().subtract(lastMaTradeInfo.getTradePrice());
                } else {
                    // 如果上一次是sell
                    profits = lastMaTradeInfo.getTradePrice().subtract(currMaTradeInfo.getTradePrice());
                }
                pureMaList.add(profits.intValue());
                if (profits.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal profitRates = profits.divide(lastMaTradeInfo.getTradePrice(), 4, BigDecimal.ROUND_HALF_UP);
                    if (profitRates.compareTo(tradeParam.getSkipTimes()) >= 0) {
                        if (currentSkip > 0) {
                            if (tradeParam.getKeepSkipAfterHuge() > currentSkip && tradeParam.getKeepSkipAfterHuge() > 0) {
                                skip.set(tradeParam.getKeepSkipAfterHuge());
                                resetSkipRecords.add(currentSkip);
                                System.out.println("ma超过大收益收益率，当前：" + currentSkip + "，重置为：" + tradeParam.getSkipAfterHuge() + "，收益：" + profits + "，交易时间:" + DateUtil.dateFormat(value.getTradeInfo().getTradeTime()));
                            }
                        } else {
                            skip.set(tradeParam.getSkipAfterHuge());
                            resetSkipRecords.add(currentSkip);
                            System.out.println("ma超过大收益收益率，当前：" + currentSkip + "，重置为：" + tradeParam.getSkipAfterHuge() + "，收益：" + profits + "，交易时间:" + DateUtil.dateFormat(value.getTradeInfo().getTradeTime()));
                        }
                    }
                }
            }
            lastMa.set(value);
        }
    }

}
