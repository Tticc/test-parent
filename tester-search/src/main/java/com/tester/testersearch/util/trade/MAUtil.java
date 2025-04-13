package com.tester.testersearch.util.trade;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.okx.OkxCommon;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * MA计算工具
 */
@Slf4j
public class MAUtil {

    public static Integer BUY_SIGN = 1;
    public static Integer SELL_SIGN = -1;
    public static Integer NONE_SIGN = 0;

    public static void calculateAndSetMA(List<TradeSignDTO> tradeSignList, int period1, int period2, int period3) {
        int period = period1;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
//                tradeSignList.get(i).setMa5(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa5(sum.divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP));
            }
        }
        period = period2;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
//                tradeSignList.get(i).setMa10(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa10(sum.divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP));
            }
        }
        period = period3;
        for (int i = 0; i < tradeSignList.size(); i++) {
            if (i < period - 1) {
//                tradeSignList.get(i).setMa20(null);
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i - period + 1; j <= i; j++) {
                    sum = sum.add(tradeSignList.get(j).getClose());
                }
                tradeSignList.get(i).setMa20(sum.divide(BigDecimal.valueOf(period),2,BigDecimal.ROUND_HALF_UP));
            }
        }
    }

    public static void calculateTradeSign(List<TradeSignDTO> tradeSignList) throws BusinessException {
        if (tradeSignList.get(0).getTradeSign() == null) {
            MAUtil.initTradeSignM5M20(tradeSignList);
        }
        Boolean lastBuySign = null;
        for (TradeSignDTO value : tradeSignList) {
            if (Objects.equals(value.getTradeSign(), 1)) {
                lastBuySign = true;
            } else if (Objects.equals(value.getTradeSign(), -1)) {
                lastBuySign = false;
            }
        }
        TradeSignDTO tradeSignDTO = tradeSignList.get(tradeSignList.size() - 1);
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(tradeSignDTO)) {
            return;
        }
        // 默认使用最新价格作为成交价格
        // BigDecimal tradePrice = tradeSignDTO.getOpen();
        BigDecimal tradePrice = tradeSignDTO.getClose();

        // 交易信号到来
        boolean tradeSignCome = false;
        if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) > 0 && (null == lastBuySign || !lastBuySign)) {
            // 如果上穿，且最近一次信号是sell。设置此次信号为BUY
            tradeSignDTO.setTradeSign(OkxCommon.BUY_SIGN);
            tradeSignDTO.setTradePrice(tradePrice);
            tradeSignDTO.setTradeTime(new Date(tradeSignDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            log.info("交易信号来临。buy。交易时间:{},交易价格:{}",DateUtil.dateFormat(tradeSignDTO.getTradeTime()),DecimalUtil.format(tradeSignDTO.getTradePrice()));
        } else if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) < 0 && (null == lastBuySign || lastBuySign)) {
            // 如果下穿，且最近一次信号是buy。设置此次信号为SELL
            tradeSignDTO.setTradeSign(OkxCommon.SELL_SIGN);
            tradeSignDTO.setTradePrice(tradePrice);
            tradeSignDTO.setTradeTime(new Date(tradeSignDTO.getLastUpdateTimestamp()));
            tradeSignCome = true;
            log.info("交易信号来临。sell。交易时间:{},交易价格:{}",DateUtil.dateFormat(tradeSignDTO.getTradeTime()),DecimalUtil.format(tradeSignDTO.getTradePrice()));
        } else {
            tradeSignDTO.setTradeSign(OkxCommon.NONE_SIGN);
        }
    }

    private static void initTradeSignM5M20(List<TradeSignDTO> tradeSignList) throws BusinessException {
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
            return;
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
}
