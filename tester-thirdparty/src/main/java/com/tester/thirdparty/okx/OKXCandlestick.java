package com.tester.thirdparty.okx;

import com.tester.testercommon.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OKXCandlestick {
    private static Map<Long, BuySellInfo> sellBuyPilot = new LinkedHashMap<>();
    private static Map<Long, DataInfo> dataInfoList = new LinkedHashMap<>();

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            createChart();
        }, 5, 1, TimeUnit.SECONDS);
    }

    public static Map<Long, DataInfo> getOKXKlineData(List<Date> timestamps, List<Double> openPrices, List<Double> highPrices, List<Double> lowPrices, List<Double> closePrices, List<Double> volumes) {
        try {
            String klineData;
            if (CollectionUtils.isEmpty(dataInfoList)) {
                klineData = OKXPositionQuery.getKlineDataWithLimit("25");
            } else {
                klineData = OKXPositionQuery.getKlineDataWithLimit("2");
            }
            JSONObject jsonResponse = new JSONObject(klineData);
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            for (int i = dataArray.length() - 1; i >= 0; i--) {
                JSONArray candle = dataArray.getJSONArray(i);
                long timestamp = candle.getLong(0);
                double openPrice = candle.getDouble(1);
                double highPrice = candle.getDouble(2);
                double lowPrice = candle.getDouble(3);
                double closePrice = candle.getDouble(4);
                double volume = candle.getDouble(5);

                DataInfo dataInfo = new DataInfo();
                dataInfo.setTimestamp(timestamp)
                        .setOpenPrice(openPrice)
                        .setHighPrice(highPrice)
                        .setLowPrice(lowPrice)
                        .setClosePrice(closePrice)
                        .setVolume(volume);
                dataInfoList.put(timestamp, dataInfo);
            }

            boolean removeOne = false;
            if (dataInfoList.size() > 300) {
                removeOne = true;
            }
            for (DataInfo value : dataInfoList.values()) {
                if (removeOne) {
                    removeOne = false;
                    continue;
                }
                timestamps.add(new Date(value.getTimestamp()));
                openPrices.add(value.getOpenPrice());
                highPrices.add(value.getHighPrice());
                lowPrices.add(value.getLowPrice());
                closePrices.add(value.getClosePrice());
                volumes.add(value.getVolume());
            }
        } catch (Exception e) {
        }
        return dataInfoList;
    }

    private static void createChart() {
        List<Date> timestamps = new ArrayList<>();
        List<Double> openPrices = new ArrayList<>();
        List<Double> highPrices = new ArrayList<>();
        List<Double> lowPrices = new ArrayList<>();
        List<Double> closePrices = new ArrayList<>();
        List<Double> volumes = new ArrayList<>();

        Map<Long, DataInfo> okxKlineData = getOKXKlineData(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);
        List<Double> ma5 = calculateMA(closePrices, 5);
        List<Double> ma10 = calculateMA(closePrices, 10);
        List<Double> ma20 = calculateMA(closePrices, 20);


        Boolean lastBuySign = null;
        for (BuySellInfo value : sellBuyPilot.values()) {
            lastBuySign = value.buySign;
        }
        for (int i = 1; i < timestamps.size(); i++) {
            if (ma5.get(i - 1) != null && ma10.get(i - 1) != null &&
                    ma5.get(i) != null && ma10.get(i) != null) {

//                double closePrice = openPrices.get(i);
                double closePrice = closePrices.get(i);
                Date candleTime = timestamps.get(i);
                long time = candleTime.getTime();

                BuySellInfo buySellInfo1 = sellBuyPilot.get(time);
                if (null != buySellInfo1) {
                    continue;
                }
                if (i == timestamps.size() - 1 && lastBuySign != null) {
                    if (ma5.get(i) > ma10.get(i) && !lastBuySign) {
                        System.out.println("Buy signal at " + candleTime + ", buy Price: " + closePrice);
                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setBuySign(true);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else if (ma5.get(i) < ma10.get(i) && lastBuySign) {
                        System.out.println("Sell signal at " + candleTime + ", Open Price: " + closePrice);
                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setBuySign(false);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else {
                        continue;
                    }
                } else {
                    if (ma5.get(i - 1) < ma10.get(i - 1) && ma5.get(i) > ma10.get(i)) {
                        System.out.println("Buy signal at " + candleTime + ", buy Price: " + closePrice);
                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setBuySign(true);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else if (ma5.get(i - 1) > ma10.get(i - 1) && ma5.get(i) < ma10.get(i)) {
                        System.out.println("Sell signal at " + candleTime + ", Open Price: " + closePrice);

                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setBuySign(false);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else {
                        continue;
                    }
                }
                for (BuySellInfo value : sellBuyPilot.values()) {
                    if (value.buySign) {
                        System.out.println("buy at " + DateUtil.dateFormat(new Date(value.getTime())) + ", with " + value.getClosePrice());
                    } else {
                        System.out.println("sell at " + DateUtil.dateFormat(new Date(value.getTime())) + ", with " + value.getClosePrice());
                    }
                }
                Double st = null;
                Double ed = null;
                double sum = 0d;
                List<Double> changeList = new ArrayList<>();
                for (BuySellInfo value : sellBuyPilot.values()) {
                    if (null == st && null == ed) {
                        st = ed = value.getClosePrice();
                    } else {
                        st = ed;
                        ed = value.getClosePrice();
                        if (!value.buySign) {
                            changeList.add(ed - st);
                            sum += (ed - st);
                        } else {
                            sum += (st - ed);
                            changeList.add(st - ed);
                        }
                    }
                }
                System.out.println("sum = " + sum);
                System.out.println("changeList = " + changeList);
                System.out.println("累计盈利:"+(sum-(double)(changeList.size()*40))+". 交易数(买+卖一次记1)：" + changeList.size()+", 交易费总计："+changeList.size()*40);
            }
        }
    }


    private static List<Double> calculateMA(List<Double> prices, int period) {
        List<Double> maValues = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            if (i < period - 1) {
                maValues.add(null);
            } else {
                double sum = 0;
                for (int j = i - period + 1; j <= i; j++) {
                    sum += prices.get(j);
                }
                maValues.add(sum / period);
            }
        }
        return maValues;
    }


    @Data
    @Accessors(chain = true)
    public static class DataInfo {
        private long timestamp;
        private double openPrice;
        private double highPrice;
        private double lowPrice;
        private double closePrice;
        private double volume;
    }

    @Data
    public static class BuySellInfo {
        private long time;
        private double closePrice;
        private boolean buySign;
        private XYTextAnnotation annotation;
    }
}
