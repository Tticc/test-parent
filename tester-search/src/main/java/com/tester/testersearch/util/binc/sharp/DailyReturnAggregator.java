package com.tester.testersearch.util.binc.sharp;

import com.tester.base.dto.exception.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class DailyReturnAggregator {

    public static class TradeProfit {
        long timestamp;
        BigDecimal profitRate;

        public TradeProfit(long timestamp, BigDecimal profitRate) {
            this.timestamp = timestamp;
            this.profitRate = profitRate;
        }
    }

    public static Map<LocalDate, BigDecimal> aggregateDailyReturns(List<TradeProfit> tradeProfits) throws BusinessException {
        // 按日期分组
        Map<LocalDate, List<BigDecimal>> profitByDay = tradeProfits.stream()
                .collect(Collectors.groupingBy(
                        trade -> Instant.ofEpochMilli(trade.timestamp).atZone(ZoneOffset.UTC).toLocalDate(),
                        LinkedHashMap::new,
                        Collectors.mapping(trade -> trade.profitRate, Collectors.toList())
                ));

        // 获取日期范围（用于填充空天为0）
        LocalDate minDate = profitByDay.keySet().stream().min(LocalDate::compareTo).orElseThrow(() -> new BusinessException(5000L));
        LocalDate maxDate = profitByDay.keySet().stream().max(LocalDate::compareTo).orElseThrow(() -> new BusinessException(5000L));

        Map<LocalDate, BigDecimal> dailyReturns = new LinkedHashMap<>();

        for (LocalDate date = minDate; !date.isAfter(maxDate); date = date.plusDays(1)) {
            List<BigDecimal> profits = profitByDay.getOrDefault(date, Collections.emptyList());

            BigDecimal dayReturn = profits.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(8, RoundingMode.HALF_UP);

            dailyReturns.put(date, dayReturn);
        }

        return dailyReturns;
    }

    public static void main(String[] args) throws BusinessException {
        List<TradeProfit> data = Arrays.asList(
                new TradeProfit(1577961000000L, new BigDecimal("0.00220000")),
                new TradeProfit(1577971800000L, new BigDecimal("-0.01070000")),
                new TradeProfit(1578025800000L, new BigDecimal("0.01570000")),
                new TradeProfit(1578346200000L, new BigDecimal("-0.00640000")),
                new TradeProfit(1578391200000L, new BigDecimal("0.03550000")),
                new TradeProfit(1578940200000L, new BigDecimal("-0.00150000")),
                new TradeProfit(1579010400000L, new BigDecimal("0.06960000")),
                new TradeProfit(1579672800000L, new BigDecimal("-0.00520000")),
                new TradeProfit(1579676400000L, new BigDecimal("-0.00470000")),
                new TradeProfit(1579681800000L, new BigDecimal("-0.01070000"))
        );

        Map<LocalDate, BigDecimal> daily = aggregateDailyReturns(data);


        daily.forEach((date, ret) ->
                System.out.println(date + " -> " + ret.toPlainString()));

        List<BigDecimal> collect = daily.values().stream().collect(Collectors.toList());
        BigDecimal annualRiskFreeRate = new BigDecimal("0.03"); // 3%
        BigDecimal sharpe = SharpeRatioCalculator.calculateSharpeRatio(collect, annualRiskFreeRate);
        System.out.println("Sharpe Ratio: " + sharpe);

        BigDecimal sqrt = SharpeRatioCalculator.sqrt(new BigDecimal(365));
        System.out.println("sqrt = " + sqrt);
    }
}
