package com.tester.testersearch.util.binc.tradeSign;

import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.binc.okx.OkxCommon;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrinterHelper {


    public static void printProfits(List<TradeSignDTO> tradeSignList) {
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
        Integer st = null;
        Integer ed = null;
        List<Integer> changeList = new ArrayList<>();
        List<Integer> profitsList = new ArrayList<>();
        List<Integer> tradeFeeList = new ArrayList<>();
        // 默认倍率
        double defaultTimes = 1.0d;
        // 默认倍率提升率。当一次交易为负数，下次提升交易数额，数额由此交易倍率决定。
        // 例如：默认数额为100，multiTimes=1.2。
        // 那么当一次交易收益为负数之后，下次交易数额为100*1.2。仍然收益为负数，下下次交易数额为100*1.2*1.2
        // 当某一次收益为正multiTimes重置为1.0
        double multiTimes = 1.0d;

        // 跳过的横盘交易次数。设置为0代表不会跳过
        int skipAfterHuge = 10;
        // 当收益超过skipNumber时，跳过未来几次的交易信号。因为每次大收益之后，都会进入横盘，因此跳过几次交易，避免部分横盘交易
        int skipNumber = 1000;
        for (TradeSignDTO value : tradeList) {
            if (null == st && null == ed) {
                st = ed = value.getTradePrice().intValue();
                if (OkxCommon.checkIfBuySign(value)) {
                    System.out.println("买入信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                } else {
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                }
            } else {
                st = ed;
                ed = value.getTradePrice().intValue();
                BigDecimal feeBig = DecimalUtil.toDecimal(value.getTradePrice()).multiply(new BigDecimal("0.0004"));
                int fee = feeBig.intValue();
                if (OkxCommon.checkIfBuySign(value)) {
                    int profits = st - ed;
                    profits = (int) (profits * defaultTimes);
//                    int fee = (int) (40 * defaultTimes);
                    if (profits < 0) {
                        defaultTimes = defaultTimes * multiTimes;
                    } else {
                        defaultTimes = 1;
                    }
                    System.out.println("收益:" + profits);
                    changeList.add(profits);
                    tradeFeeList.add(fee);
                    System.out.println("买入信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                } else {
                    int profits = ed - st;
                    profits = (int) (profits * defaultTimes);
//                    int fee = (int) (40 * defaultTimes);
                    if (profits < 0) {
                        defaultTimes = defaultTimes * multiTimes;
                    } else {
                        defaultTimes = 1;
                    }
                    System.out.println("收益:" + profits);
                    changeList.add(profits);
                    tradeFeeList.add(fee);
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                }
            }
            BigDecimal multiply = DecimalUtil.toDecimal(value.getTradePrice()).multiply(new BigDecimal("0.012"));
            skipNumber = multiply.intValue();
        }
        Integer sum = 0;
        Integer feeSum = 0;
        int skip = 0;
        for (int i = 0; i < changeList.size(); i++) {
            if (skip > 0) {
                skip -= 1;
                continue;
            }
            sum += changeList.get(i);
            feeSum += tradeFeeList.get(i);
            profitsList.add(changeList.get(i));
            if (changeList.get(i) >= skipNumber) {
                skip = skipAfterHuge;
            }
        }
        Collections.reverse(changeList);
        Collections.reverse(profitsList);
        System.out.println("changeList.size():" + changeList.size() + ", changeList = " + changeList);
        System.out.println("profitList.size():" + profitsList.size() + ", profitList = " + profitsList);
        System.out.println("sum = " + sum);
        System.out.println("累计盈利:" + (sum - feeSum) + ". 交易数(买+卖一次记1)：" + profitsList.size() + ", 交易费总计：" + feeSum);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }


    public static void printProfitsWithTPSL(List<TradeSignDTO> tradeSignList, int skipAfterHuge, int keepSkipAfterHuge, BigDecimal skipTimes) {
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
        Integer st = null;
        Integer ed = null;
        List<Integer> changeList = new ArrayList<>();
        List<Integer> profitsList = new ArrayList<>();
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
        boolean lastIsMa = true;
        Integer sum = 0;
        Integer feeSum = 0;
        AtomicInteger skip = new AtomicInteger(0);
        Date lastTime = null;
        for (TradeSignDTO value : tradeList) {
            if (null == st && null == ed) {
                st = ed = value.getTradePrice().intValue();
                lastTime = value.getTradeTime();
                if (OkxCommon.checkIfBuySign(value)) {
                    System.out.println("买入信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(value.getTradeTime()));
                } else {
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(value.getTradeTime()));
                }
            } else {
                st = ed;
                ed = value.getTradePrice().intValue();
                BigDecimal feeBig = DecimalUtil.toDecimal(value.getTradePrice()).multiply(new BigDecimal("0.0004"));
                int fee = feeBig.intValue();
                if (OkxCommon.checkIfMABuySign(value)) {
                    if (lastIsMa) {
                        int profits = st - ed;
                        System.out.println("收益:" + profits);
                        System.out.println("买入信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(value.getTradeTime()));
//                        printInfo(value, st, lastTime, true);
                        peocessList(changeList, profitsList, tradeFeeList, profits, fee, skip, defaultSkipNumber, skipAfterHuge, keepSkipAfterHuge);
                    } else {
                        System.out.println("单边买入信号。价格:" + value.getTradePrice() + DateUtil.dateFormat(value.getTradeTime()));
                    }
                    lastIsMa = true;
                } else if (OkxCommon.checkIfTPSLBuySign(value)) {
                    lastIsMa = false;
                    int profits = st - ed;
                    System.out.println("收益:" + profits);
                    System.out.println("止盈止损买入信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(value.getTradeTime()));
//                    printInfo(value, st, lastTime, true);
                    peocessList(changeList, profitsList, tradeFeeList, profits, fee, skip, defaultSkipNumber, skipAfterHuge, keepSkipAfterHuge);
                } else if (OkxCommon.checkIfMASellSign(value)) {
                    if (lastIsMa) {
                        int profits = ed - st;
                        System.out.println("收益:" + profits);
                        System.out.println("卖出信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(value.getTradeTime()));
//                        printInfo(value, st, lastTime, false);
                        peocessList(changeList, profitsList, tradeFeeList, profits, fee, skip, defaultSkipNumber, skipAfterHuge, keepSkipAfterHuge);
                    } else {
                        System.out.println("单边卖出信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(value.getTradeTime()));
                    }
                    lastIsMa = true;
                } else if (OkxCommon.checkIfTPSLSellSign(value)) {
                    lastIsMa = false;
                    int profits = ed - st;
                    System.out.println("收益:" + profits);
                    System.out.println("止盈止损卖出信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(value.getTradeTime()));
//                    printInfo(value, st, lastTime, false);
                    peocessList(changeList, profitsList, tradeFeeList, profits, fee, skip, defaultSkipNumber, skipAfterHuge, keepSkipAfterHuge);
                }
                lastTime = value.getTradeTime();
            }
            BigDecimal multiply = DecimalUtil.toDecimal(value.getTradePrice()).multiply(skipTimes);
            defaultSkipNumber = multiply.intValue();
        }
        for (int i = 0; i < profitsList.size(); i++) {
            sum += profitsList.get(i);
            feeSum += tradeFeeList.get(i);
        }
        Collections.reverse(changeList);
        Collections.reverse(profitsList);
        System.out.println("skip:" + skip.get());
        System.out.println("changeList.size():" + changeList.size() + ", changeList = " + changeList);
        System.out.println("profitList.size():" + profitsList.size() + ", profitList = " + profitsList);
        System.out.println("sum = " + sum);
        System.out.println("累计盈利:" + (sum - feeSum) + ". 交易数(买+卖一次记1)：" + profitsList.size() + ", 交易费总计：" + feeSum);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void printInfo(TradeSignDTO value, int st, Date lastDate, boolean ifBuy){
        BigDecimal stBig = new BigDecimal(st);
        BigDecimal edBig = value.getTradePrice();
        Instant startInstant = lastDate.toInstant();
        Instant endInstant = value.getTradeTime().toInstant();
        Duration duration = Duration.between(startInstant, endInstant);
        double time = duration.getSeconds() / 3600.0; // 注意用 3600.0 保留小数

        BigDecimal rate;
        String tag = "";
        if(ifBuy) {
            rate = stBig.subtract(edBig).divide(stBig, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            tag = "空头";
        }else{
            rate = edBig.subtract(stBig).divide(edBig, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            tag = "多头";
        }
        System.out.println(DateUtil.dateFormat(value.getTradeTime())+"\t"+tag+"\t"+stBig+"\t"+value.getTradePrice().intValue()+"\t"+rate+"\t"+String.format("%.2f", time));
    }

    private static void peocessList(List<Integer> changeList
            , List<Integer> profitsList
            , List<Integer> tradeFeeList
            , int profits
            , int fee
            , AtomicInteger skip
            , int skipNumber
            , int skipAfterHuge
            , int keepSkipAfterHuge) {
        changeList.add(profits);
        if (skip.get() > 0) {
            int leftSkip = skip.decrementAndGet();
            if (profits >= skipNumber && keepSkipAfterHuge > leftSkip && keepSkipAfterHuge > 0) {
                System.out.println("剩余skip："+leftSkip+",重置skip为："+keepSkipAfterHuge);
                skip.set(keepSkipAfterHuge);
            }
            return;
        }
        tradeFeeList.add(fee);
        profitsList.add(profits);
        if (profits >= skipNumber) {
            skip.set(skipAfterHuge);
        }
    }
}
