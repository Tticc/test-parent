package com.tester.testersearch.util.binc.sharp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SharpeRatioCalculator {

    /**
     * 计算夏普比率（365 天年化）
     *
     * @param returns 每日收益列表（如 0.002 表示 0.2%）
     * @param annualRiskFreeRate 年化无风险利率（如 0.03 表示 3%）
     * @return 夏普比率（BigDecimal，保留 6 位小数）
     */
    public static BigDecimal calculateSharpeRatio(List<BigDecimal> returns, BigDecimal annualRiskFreeRate) {
        if (returns == null || returns.size() < 2) {
            throw new IllegalArgumentException("收益数据不足");
        }

        BigDecimal riskFreeDailyRate = annualRiskFreeRate.divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);

        // 计算平均日收益
        BigDecimal totalReturn = BigDecimal.ZERO;
        for (BigDecimal r : returns) {
            totalReturn = totalReturn.add(r);
        }
        BigDecimal averageReturn = totalReturn.divide(BigDecimal.valueOf(returns.size()), 10, RoundingMode.HALF_UP);

        // 计算标准差（日收益波动率）
        BigDecimal sumSquaredDiff = BigDecimal.ZERO;
        for (BigDecimal r : returns) {
            BigDecimal diff = r.subtract(averageReturn);
            sumSquaredDiff = sumSquaredDiff.add(diff.multiply(diff));
        }
        BigDecimal stdDev = sqrt(sumSquaredDiff.divide(BigDecimal.valueOf(returns.size() - 1), 10, RoundingMode.HALF_UP));

        if (stdDev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // 避免除以0
        }

        // 超额收益
        BigDecimal excessReturn = averageReturn.subtract(riskFreeDailyRate);

        // 夏普比率 = 超额日收益 / 标准差 × √365
        BigDecimal sqrt365 = sqrt(BigDecimal.valueOf(365));
        return excessReturn.divide(stdDev, 10, RoundingMode.HALF_UP)
                .multiply(sqrt365)
                .setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * 计算 BigDecimal 的平方根（使用牛顿迭代法）
     */
    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));
        BigDecimal two = BigDecimal.valueOf(2);
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, 10, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(two, 10, RoundingMode.HALF_UP);
        }
        return x1;
    }

    // 示例用法
//    public static void main(String[] args) {
//        List<BigDecimal> returns = List.of(
//                new BigDecimal("0.002"),
//                new BigDecimal("-0.001"),
//                new BigDecimal("0.0015"),
//                new BigDecimal("0.0005"),
//                new BigDecimal("-0.0007")
//        );
//
//        BigDecimal annualRiskFreeRate = new BigDecimal("0.03"); // 3%
//
//        BigDecimal sharpe = calculateSharpeRatio(returns, annualRiskFreeRate);
//        System.out.println("Sharpe Ratio: " + sharpe);
//    }
}
