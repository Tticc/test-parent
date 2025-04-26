package com.tester.testersearch.util.binc.okx;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OkxCommon {

    public static void main(String[] args) {
        BigDecimal bi = new BigDecimal("5.5");
        System.out.println("bi = " + bi);
        int i = bi.intValue();
        System.out.println("i = " + i);
    }


    public static Integer BUY_SIGN = 1;
    public static Integer SELL_SIGN = -1;
    public static Integer NONE_SIGN = 0;

    public static void printProfits(List<TradeSignDTO> tradeSignList) {
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> checkIfHasTradeSign(e)).collect(Collectors.toList());
        Integer st = null;
        Integer ed = null;
        List<Integer> changeList = new ArrayList<>();
        List<Integer> profitList = new ArrayList<>();
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
                if (checkIfBuySign(value)) {
                    System.out.println("买入信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                } else {
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                }
            } else {
                st = ed;
                ed = value.getTradePrice().intValue();
                if (checkIfBuySign(value)) {
                    int profits = st - ed;
                    profits = (int) (profits * defaultTimes);
                    int fee = (int) (40 * defaultTimes);
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
                    int fee = (int) (40 * defaultTimes);
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
            profitList.add(changeList.get(i));
            if (changeList.get(i) >= skipNumber) {
                skip = skipAfterHuge;
            }
        }
        System.out.println("changeList = " + changeList);
        System.out.println("profitList.size():"+profitList.size()+", profitList = " + profitList);
        System.out.println("sum = " + sum);
        System.out.println("累计盈利:" + (sum - feeSum) + ". 交易数(买+卖一次记1)：" + changeList.size() + ", 交易费总计：" + feeSum);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void printProfits_anti(List<TradeSignDTO> tradeSignList) {
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> checkIfHasTradeSign(e)).collect(Collectors.toList());
        Integer st = null;
        Integer ed = null;
        List<Integer> changeList = new ArrayList<>();
        for (TradeSignDTO value : tradeList) {
            if (null == st && null == ed) {
                st = ed = value.getTradePrice().intValue();
                if (checkIfBuySign(value)) {
                    System.out.println("买入信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                } else {
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + ", open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                }
            } else {
                st = ed;
                ed = value.getTradePrice().intValue();
                if (checkIfBuySign(value)) {
                    int profits = st - ed;
                    System.out.println("收益:" + profits);
//                    if(profits <= -500){
//                        profits = -500;
//                    }
                    changeList.add(profits);
                    System.out.println("买入信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                } else {
                    int profits = ed - st;
                    System.out.println("收益:" + profits);
//                    if(profits <= -500){
//                        profits = -500;
//                    }
                    changeList.add(profits);
                    System.out.println("卖出信号。价格:" + value.getTradePrice() + "(上一轮" + st + "), open时间:" + DateUtil.dateFormat(new Date(value.getOpenTimestamp())));
                }
            }
        }
        Integer sum = changeList.stream().reduce(0, Integer::sum);
        System.out.println("changeList = " + changeList);
        System.out.println("sum = " + sum);
        System.out.println("累计盈利:" + (sum - changeList.size() * 40) + ". 交易数(买+卖一次记1)：" + changeList.size() + ", 交易费总计：" + changeList.size() * 40);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void initTradeSignM5M20(List<TradeSignDTO> tradeSignList) throws BusinessException {
        long lastSignTime = -1L;
        TradeSignDTO lastSignT = null;
        for (int i = 1; i < tradeSignList.size(); i++) {
            TradeSignDTO tradeSignDTO = tradeSignList.get(i);
            if (tradeSignList.get(i - 1).getMa5() == null || tradeSignList.get(i - 1).getMa20() == null ||
                    tradeSignList.get(i).getMa5() == null || tradeSignList.get(i).getMa20() == null) {
                continue;
            }
            if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa20()) < 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa20()) > 0) {
                tradeSignDTO.setTradeSign(BUY_SIGN);
                tradeSignDTO.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
                lastSignT = tradeSignDTO;
            } else if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa20()) > 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa20()) < 0) {
                tradeSignDTO.setTradeSign(SELL_SIGN);
                tradeSignDTO.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
                lastSignT = tradeSignDTO;
            } else {
                continue;
            }
        }
        if (lastSignTime < 0) {
            throw new BusinessException(5000L, "初始化时未找到交易信号。请加大范围后重试");
        }
        for (TradeSignDTO tradeSignDTO : tradeSignList) {
            if (tradeSignDTO.getTimestamp() < lastSignTime) {
                tradeSignDTO.setTradePrice(null);
                tradeSignDTO.setTradeSign(NONE_SIGN);
            } else if (tradeSignDTO.getTimestamp() > lastSignTime) {
                tradeSignDTO.setTradePrice(null);
                tradeSignDTO.setTradeSign(NONE_SIGN);
            }
        }
        lastSignT.setTradeTime(new Date(lastSignTime));
        System.out.println("交易信号数据初始化完成。最近一次交易信号出现时间：" + DateUtil.dateFormat(new Date(lastSignTime)));
        System.out.println("类型：" + lastSignT.getTradeSign());
        System.out.println("金额：" + DecimalUtil.format(lastSignT.getClose()));
    }


    public static void initTradeSignM5M10(List<TradeSignDTO> tradeSignList) throws BusinessException {
        long lastSignTime = -1L;
        TradeSignDTO lastSignT = null;
        for (int i = 1; i < tradeSignList.size(); i++) {
            TradeSignDTO tradeSignDTO = tradeSignList.get(i);
            if (tradeSignList.get(i - 1).getMa5() == null || tradeSignList.get(i - 1).getMa10() == null ||
                    tradeSignList.get(i).getMa5() == null || tradeSignList.get(i).getMa10() == null) {
                continue;
            }
            if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa10()) < 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa10()) > 0) {
                tradeSignDTO.setTradeSign(OkxCommon.BUY_SIGN);
                tradeSignDTO.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
                lastSignT = tradeSignDTO;
            } else if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa10()) > 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa10()) < 0) {
                tradeSignDTO.setTradeSign(OkxCommon.SELL_SIGN);
                tradeSignDTO.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
                lastSignT = tradeSignDTO;
            } else {
                continue;
            }
        }
        if (lastSignTime < 0) {
            throw new BusinessException(5000L, "初始化时未找到交易信号。请加大范围后重试");
        }
        for (TradeSignDTO tradeSignDTO : tradeSignList) {
            if (tradeSignDTO.getTimestamp() < lastSignTime) {
                tradeSignDTO.setTradePrice(null);
                tradeSignDTO.setTradeSign(OkxCommon.NONE_SIGN);
            } else if (tradeSignDTO.getTimestamp() > lastSignTime) {
                tradeSignDTO.setTradePrice(null);
                tradeSignDTO.setTradeSign(OkxCommon.NONE_SIGN);
            }
        }
        lastSignT.setTradeTime(new Date(lastSignTime));
        System.out.println("交易信号数据初始化完成。最近一次交易信号出现时间：" + DateUtil.dateFormat(new Date(lastSignTime)));
        System.out.println("类型：" + lastSignT.getTradeSign());
        System.out.println("金额：" + DecimalUtil.format(lastSignT.getClose()));
    }


    public static boolean checkIfHasTradeSign(TradeSignDTO tradeSignDTO) {
        return checkIfBuySign(tradeSignDTO) || checkIfSellSign(tradeSignDTO);
    }

    public static boolean checkIfBuySign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), BUY_SIGN);
    }

    public static boolean checkIfSellSign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), SELL_SIGN);
    }


    public static void calculateAndSetMA(List<TradeSignDTO> tradeSignList) {
        calculateAndSetMA(tradeSignList, 5, 10, 20);
    }

    public static void calculateAndSetMA(List<TradeSignDTO> tradeSignList, int period1, int period2, int period3) {
        int period = period1;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
                tradeSignList.get(i).setMa5(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa5(sum.divide(BigDecimal.valueOf(period), 2, BigDecimal.ROUND_HALF_UP));
            }
        }
        period = period2;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
                tradeSignList.get(i).setMa10(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa10(sum.divide(BigDecimal.valueOf(period), 2, BigDecimal.ROUND_HALF_UP));
            }
        }
        period = period3;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
                tradeSignList.get(i).setMa20(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa20(sum.divide(BigDecimal.valueOf(period), 2, BigDecimal.ROUND_HALF_UP));
            }
        }
    }


}
