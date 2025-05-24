package com.tester.testersearch.util.binc.tradeSign;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.BeanCopyUtil;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * MA计算工具
 */
@Slf4j
public class MAUtil {

    public static Integer BUY_SIGN = 1;
    public static Integer SELL_SIGN = -1;
    public static Integer NONE_SIGN = 0;

    public static void calculateAndSetMA(List<TradeSignDTO> tradeSignList, TradeParam tradeParam, boolean first) {
        int period = tradeParam.getMaShort();
        int skipNum = -1;
        if(!first){
            skipNum = tradeSignList.size()-2;
        }
        for (int i = 0; i < tradeSignList.size(); i++) {
            if(i < skipNum){
                continue;
            }
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
        period = tradeParam.getMaMiddle();
        for (int i = 0; i < tradeSignList.size(); i++) {
            if(i < skipNum){
                continue;
            }
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
        period = tradeParam.getMaLong();
        for (int i = 0; i < tradeSignList.size(); i++) {
            if(i < skipNum){
                continue;
            }
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

    public static void initTradeSignM5M20(List<TradeSignDTO> tradeSignList) throws BusinessException {
        TradeSignDTO.TradeInfo lastTradeInfo = new TradeSignDTO.TradeInfo();
        long lastSignTime = -1L;
        for (int i = 1; i < tradeSignList.size(); i++) {
            TradeSignDTO tradeSignDTO = tradeSignList.get(i);
            if (tradeSignList.get(i - 1).getMa5() == null || tradeSignList.get(i - 1).getMa20() == null ||
                    tradeSignList.get(i).getMa5() == null || tradeSignList.get(i).getMa20() == null) {
                continue;
            }
            if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa20()) <= 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa20()) > 0) {
                lastTradeInfo.setTradeSign(TradeSignEnum.BUY_SIGN.getCode());
                lastTradeInfo.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
            } else if (tradeSignList.get(i - 1).getMa5().compareTo(tradeSignList.get(i - 1).getMa20()) >= 0
                    && tradeSignList.get(i).getMa5().compareTo(tradeSignList.get(i).getMa20()) < 0) {
                lastTradeInfo.setTradeSign(TradeSignEnum.SELL_SIGN.getCode());
                lastTradeInfo.setTradePrice(tradeSignDTO.getClose());
                lastSignTime = tradeSignDTO.getTimestamp();
            } else {
                continue;
            }
        }
        if (lastSignTime < 0) {
            return;
        }
        for (TradeSignDTO tradeSignDTO : tradeSignList) {
            if(Objects.equals(tradeSignDTO.getTimestamp(), lastSignTime)){
                // 填充并设置正向交易信息
                lastTradeInfo.setTradeTime(new Date(lastSignTime))
                        .setTradeEnd(ConstantList.ZERO)
                        .setTradeStart(ConstantList.ONE)
                        .setTradeProfits(BigDecimal.ZERO)
                        .setTradeProfitsRate(BigDecimal.ZERO)
                        .setTradeSerialNum(ConstantList.ONE);
                tradeSignDTO.setTradeInfo(lastTradeInfo);
                tradeSignDTO.setPureTradeInfo(lastTradeInfo);

                // 填充并设置逆向交易信息
                TradeSignDTO.TradeInfo lastReverseTradeInfo = new TradeSignDTO.TradeInfo();
                BeanCopyUtil.copyPropertiesIgnoreNull(lastTradeInfo, lastReverseTradeInfo);
                // TradeSign设置为正向交易相反即可
                lastReverseTradeInfo.setTradeSign(lastTradeInfo.getTradeSign()*-1);
                tradeSignDTO.setReverseTradeInfo(lastReverseTradeInfo);
                tradeSignDTO.setActualTradeInfo(lastTradeInfo);
                break;
            }
        }
        System.out.println("交易信号数据初始化完成。最近一次交易信号出现时间：" + DateUtil.dateFormat(new Date(lastSignTime)));
        System.out.println("类型：" + lastTradeInfo.getTradeSign());
        System.out.println("金额：" + DecimalUtil.format(lastTradeInfo.getTradePrice()));
    }
}
