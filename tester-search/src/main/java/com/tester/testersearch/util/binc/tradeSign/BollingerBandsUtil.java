package com.tester.testersearch.util.binc.tradeSign;

import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 布林线计算工具
 */
public class BollingerBandsUtil {

    public static void calculateBollingerBands(List<TradeSignDTO> tradeSignList, int period, double stdDevMultiplier) {
        for (int i = 0; i < tradeSignList.size(); i++) {
            // 前 period-1 个元素无法计算，无需处理
            if (i < period - 1) {
                continue;
            }
            TradeSignDTO tradeSignDTO = tradeSignList.get(i);
            // 计算移动平均 MA
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i - period + 1; j <= i; j++) {
                sum = sum.add(DecimalUtil.toDecimal(tradeSignList.get(j).getClose()));
            }
            BigDecimal ma = sum.divide(BigDecimal.valueOf(period), 2, BigDecimal.ROUND_HALF_UP);

            // 计算标准差
            BigDecimal variance = BigDecimal.ZERO;
            for (int j = i - period + 1; j <= i; j++) {
                BigDecimal diff = DecimalUtil.toDecimal(tradeSignList.get(j).getClose()).subtract(ma);
                variance = variance.add(diff.multiply(diff)); // 等同于 Math.pow(diff, 2)
            }
            BigDecimal divide = variance.divide(BigDecimal.valueOf(period), 2, BigDecimal.ROUND_HALF_UP);
            double stdDev = Math.sqrt(divide.doubleValue());

            // 计算上下轨
            tradeSignDTO.setMiddleBand(ma);
            tradeSignDTO.setUpperBand(new BigDecimal(ma.doubleValue() + stdDevMultiplier * stdDev));
            tradeSignDTO.setLowerBand(new BigDecimal(ma.doubleValue() - stdDevMultiplier * stdDev));
        }
    }
}
