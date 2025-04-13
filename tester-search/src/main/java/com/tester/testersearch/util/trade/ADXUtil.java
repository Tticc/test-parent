package com.tester.testersearch.util.trade;

import com.tester.testercommon.util.DecimalUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * ADX计算工具
 */
public class ADXUtil {


    public static void calculateADX(List<TradeSignDTO> klineData, int period) {
        List<BigDecimal> plusDM = new ArrayList<>();
        List<BigDecimal> minusDM = new ArrayList<>();
        List<BigDecimal> trueRange = new ArrayList<>();
        List<BigDecimal> plusDI = new ArrayList<>();
        List<BigDecimal> minusDI = new ArrayList<>();
        List<BigDecimal> DX = new ArrayList<>();

        plusDM.add(null);
        minusDM.add(null);
        trueRange.add(null);

        for (int i = 1; i < klineData.size(); i++) {
            TradeSignDTO current = klineData.get(i);
            TradeSignDTO prev = klineData.get(i - 1);

            BigDecimal upMove = current.getHigh().subtract(prev.getHigh());
            BigDecimal downMove = prev.getLow().subtract(current.getLow());

            // 计算 +DM 和 -DM
            plusDM.add((upMove.compareTo(downMove) > 0 && upMove.compareTo(BigDecimal.ZERO) > 0) ? upMove : BigDecimal.ZERO);
            minusDM.add((downMove.compareTo(upMove) > 0 && downMove.compareTo(BigDecimal.ZERO) > 0) ? downMove : BigDecimal.ZERO);

            // 计算 TR（真实波动范围）
            BigDecimal range1 = current.getHigh().subtract(current.getLow()).abs();
            BigDecimal range2 = current.getHigh().subtract(prev.getClose()).abs();
            BigDecimal range3 = current.getLow().subtract(prev.getClose()).abs();
            BigDecimal tr = range1.max(range2).max(range3);
            trueRange.add(tr);
        }

        List<BigDecimal> smoothedPlusDM = smoothedMovingAverage(plusDM, period);
        List<BigDecimal> smoothedMinusDM = smoothedMovingAverage(minusDM, period);
        List<BigDecimal> smoothedTR = smoothedMovingAverage(trueRange, period);

        // 计算 +DI 和 -DI
        for (int i = 0; i < smoothedPlusDM.size(); i++) {
            BigDecimal tr = smoothedTR.get(i);
            if (tr == null || tr.compareTo(BigDecimal.ZERO) == 0) {
                plusDI.add(null);
                minusDI.add(null);
            } else {
                plusDI.add(smoothedPlusDM.get(i).divide(tr, 3, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
                minusDI.add(smoothedMinusDM.get(i).divide(tr, 3, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
            }
        }

        for (int i = 0; i < plusDI.size(); i++) {
            BigDecimal pdi = plusDI.get(i);
            BigDecimal mdi = minusDI.get(i);
            if (pdi == null || mdi == null) {
                DX.add(null);
            } else {
                BigDecimal dx = (pdi.subtract(mdi)).abs()
                        .divide(pdi.add(mdi), 10, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                DX.add(dx);
            }
        }
        List<BigDecimal> ADX = smoothedMovingAverage(DX, period);
        for (int i = 0; i < klineData.size(); i++) {
            TradeSignDTO tradeSignDTO = klineData.get(i);
            if (plusDI.get(i) != null) {
                tradeSignDTO.setPlusDI(plusDI.get(i));
            }
            if (minusDI.get(i) != null) {
                tradeSignDTO.setMinusDI(minusDI.get(i));
            }
            if (i < (klineData.size() - 2)) {
                // 如果不是最后2个ADX节点，且ADX是null，则更新，否则不更新
                if (ADX.get(i) != null && tradeSignDTO.getADX() == null) {
                    tradeSignDTO.setADX(ADX.get(i));
                }
            } else {
                // 如果是最后2个ADX节点，更新
                if (ADX.get(i) != null) {
                    tradeSignDTO.setADX(ADX.get(i));
                }
            }
        }
    }

    // SMA据说更适合长线
    // 计算 14 日滑动平均值
    public static List<BigDecimal> smoothedMovingAverage(List<BigDecimal> data, int period) {
        List<BigDecimal> result = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < data.size(); i++) {
            sum = sum.add(DecimalUtil.toDecimal(data.get(i)));
            if (i >= period - 1) {
                result.add(sum.divide(new BigDecimal(period), 10, RoundingMode.HALF_UP));
                sum = sum.subtract(DecimalUtil.toDecimal(data.get(i - period + 1)));
            } else {
                result.add(null);
            }
        }
        return result;
    }

    // EMA据说更适合短线
    public static List<BigDecimal> exponentialMovingAverage(List<BigDecimal> data, int period) {
        List<BigDecimal> result = new ArrayList<>();
        BigDecimal multiplier = new BigDecimal("2")
                .divide(new BigDecimal(period + 1), 3, RoundingMode.HALF_UP);
        BigDecimal ema = null;

        // 计算第一个 EMA 值（使用 period 内非空值的 SMA）
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (int i = 0; i < period && i < data.size(); i++) {
            BigDecimal value = data.get(i);
            if (value != null) {
                sum = sum.add(value);
                count++;
            }
        }
        if (count > 0) {
            ema = sum.divide(new BigDecimal(count), 3, RoundingMode.HALF_UP);
        }

        for (int i = 0; i < data.size(); i++) {
            BigDecimal current = data.get(i);
            if (i < period - 1) {
                result.add(null);
            } else if (i == period - 1) {
                result.add(ema);
            } else {
                if (current != null) {
                    ema = current.subtract(DecimalUtil.toDecimal(ema))
                            .multiply(multiplier)
                            .add(DecimalUtil.toDecimal(ema))
                            .setScale(3, RoundingMode.HALF_UP);
                }
                result.add(ema);
            }
        }
        return result;
    }
}
