package com.tester.thirdparty.okx;

import com.tester.testercommon.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OKXCandlestickChart {
    private static JFrame frame;
    private static ChartPanel chartPanel;
    private static JLabel infoLabel;
    private static Map<Long, BuySellInfo> sellBuyPilot = new LinkedHashMap<>();
    private static Map<Long, DataInfo> dataInfoList = new LinkedHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("OKX BTC/USDT 5m K线 + MA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLayout(new BorderLayout());

            // 初始化时创建一次图表
            JFreeChart chart = createChart();
            chartPanel = new ChartPanel(chart);
            chartPanel.setMouseWheelEnabled(true);
            frame.add(chartPanel, BorderLayout.CENTER);

            infoLabel = new JLabel("鼠标悬停查看蜡烛数据");
            frame.add(infoLabel, BorderLayout.SOUTH);
            addMouseHoverListener(chartPanel);
            frame.setVisible(true);

            // 使用单线程更新图表
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                JFreeChart updatedChart = createChart();
                SwingUtilities.invokeLater(() -> {
                    // 直接更新现有ChartPanel的内容
                    chartPanel.setChart(updatedChart);
                    chartPanel.repaint();
                });
            }, 5, 1, TimeUnit.SECONDS);
        });
    }

    @Data
    public static class BuySellInfo{
        private long time;
        private double closePrice;
        private boolean buySign;
        private XYTextAnnotation annotation;
    }
    public static Map<Long, DataInfo> getOKXKlineData(List<Date> timestamps, List<Double> openPrices, List<Double> highPrices, List<Double> lowPrices, List<Double> closePrices, List<Double> volumes) {
        String apiUrl = "https://www.okx.com/api/v5/market/candles?instId=BTC-USDT&bar=5m&limit=100";

        try {
            String klineData;
            if(CollectionUtils.isEmpty(dataInfoList)) {
                klineData = OKXPositionQuery.getKlineDataWithLimit("25");
            }else {
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
            if(dataInfoList.size() > 300){
                removeOne = true;
            }
            for (DataInfo value : dataInfoList.values()) {
                if(removeOne){
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
            log.error("数据解析失败");
        }
        return dataInfoList;
    }

    @Data
    @Accessors(chain = true)
    public static class DataInfo{
        private long timestamp;
        private double openPrice;
        private double highPrice;
        private double lowPrice;
        private double closePrice;
        private double volume;
    }

    private static JFreeChart createChart() {
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

        DefaultHighLowDataset dataset = createCandlestickDataset(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);
        TimeSeriesCollection maDataset = new TimeSeriesCollection();
        addMAToDataset(maDataset, "MA5", timestamps, ma5);
        addMAToDataset(maDataset, "MA10", timestamps, ma10);
        addMAToDataset(maDataset, "MA20", timestamps, ma20);

        JFreeChart chart = createCombinedChart(dataset, maDataset);
        XYPlot plot = (XYPlot) chart.getPlot();

        // 清理旧标记后添加新标记
        plot.clearAnnotations();

        Boolean lastBuySign = null;
        for (BuySellInfo value : sellBuyPilot.values()) {
            lastBuySign = value.buySign;
        }
        for (int i = 1; i < timestamps.size(); i++) {
            if (ma5.get(i - 1) != null && ma10.get(i - 1) != null &&
                    ma5.get(i) != null && ma10.get(i) != null) {

                int openIndex = i+1 >= openPrices.size() ? openPrices.size()-1 : i+1;
                double openPrice = openPrices.get(i);
                double closePrice = closePrices.get(i);
                Date candleTime = timestamps.get(i);
                long time = candleTime.getTime();

                BuySellInfo buySellInfo1 = sellBuyPilot.get(time);
                if(null != buySellInfo1){
                    plot.addAnnotation(buySellInfo1.getAnnotation());
                    continue;
                }

                if(i == timestamps.size()-1 && lastBuySign != null){
                    if(ma5.get(i) >= ma10.get(i) && !lastBuySign){
                        System.out.println("Buy signal at " + candleTime + ", buy Price: " + closePrice);
                        XYTextAnnotation annotation = new XYTextAnnotation("Buy" + closePrice, candleTime.getTime(), closePrice);
                        annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
                        annotation.setPaint(Color.BLUE);
                        plot.addAnnotation(annotation);

                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setAnnotation(annotation);
                        buySellInfo.setBuySign(true);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    }else if(ma5.get(i) < ma10.get(i) && lastBuySign){
                        System.out.println("Sell signal at " + candleTime + ", Open Price: " + closePrice);
                        XYTextAnnotation annotation = new XYTextAnnotation("Sell" + closePrice, candleTime.getTime(), closePrice);
                        annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
                        annotation.setPaint(Color.BLACK);
                        plot.addAnnotation(annotation);


                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setAnnotation(annotation);
                        buySellInfo.setBuySign(false);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    }else{
                        continue;
                    }
                }else {
                    if (ma5.get(i - 1) < ma10.get(i - 1) && ma5.get(i) >= ma10.get(i)) {
                        System.out.println("Buy signal at " + candleTime + ", buy Price: " + closePrice);
                        XYTextAnnotation annotation = new XYTextAnnotation("Buy" + closePrice, candleTime.getTime(), closePrice);
                        annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
                        annotation.setPaint(Color.BLUE);
                        plot.addAnnotation(annotation);

                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setAnnotation(annotation);
                        buySellInfo.setBuySign(true);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else if (ma5.get(i - 1) > ma10.get(i - 1) && ma5.get(i) <= ma10.get(i)) {
                        System.out.println("Sell signal at " + candleTime + ", Open Price: " + closePrice);
                        XYTextAnnotation annotation = new XYTextAnnotation("Sell" + closePrice, candleTime.getTime(), closePrice);
                        annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
                        annotation.setPaint(Color.BLACK);
                        plot.addAnnotation(annotation);


                        BuySellInfo buySellInfo = new BuySellInfo();
                        buySellInfo.setAnnotation(annotation);
                        buySellInfo.setBuySign(false);
                        buySellInfo.setClosePrice(closePrice);
                        buySellInfo.setTime(time);
                        sellBuyPilot.put(time, buySellInfo);
                    } else {
                        continue;
                    }
                }
                for (BuySellInfo value : sellBuyPilot.values()) {
                    if(value.buySign){
                        System.out.println("buy at "+DateUtil.dateFormat( new Date(value.getTime())) +"， with "+value.getClosePrice());
                    }else{
                        System.out.println("sell at "+DateUtil.dateFormat( new Date(value.getTime())) +"， with "+value.getClosePrice());
                    }
                }
                Double st = null;
                Double ed = null;
                double sum = 0d;
                List<Double> changeList = new ArrayList<>();
                for (BuySellInfo value : sellBuyPilot.values()) {
                    if(null == st && null == ed){
                        st = ed = value.getClosePrice();
                    }else{
                        st=ed;
                        ed=value.getClosePrice();
                        if(!value.buySign){
                            changeList.add(ed-st);
                            sum+=(ed-st);
                        }else{
                            sum+=(st-ed);
                            changeList.add(st-ed);
                        }
                    }
                }
                System.out.println("sum = " + sum);
                System.out.println("changeList = " + changeList);
            }
        }
        return chart;
    }
    public static DefaultHighLowDataset createCandlestickDataset(List<Date> timestamps, List<Double> open, List<Double> high, List<Double> low, List<Double> close, List<Double> volume) {
        Date[] dateArr = timestamps.toArray(new Date[0]);
        double[] openArr = open.stream().mapToDouble(Double::doubleValue).toArray();
        double[] highArr = high.stream().mapToDouble(Double::doubleValue).toArray();
        double[] lowArr = low.stream().mapToDouble(Double::doubleValue).toArray();
        double[] closeArr = close.stream().mapToDouble(Double::doubleValue).toArray();
        double[] volumeArr = volume.stream().mapToDouble(Double::doubleValue).toArray();

        return new DefaultHighLowDataset("Candlestick", dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
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

    private static void addMAToDataset(TimeSeriesCollection dataset, String name, List<Date> timestamps, List<Double> maValues) {
        TimeSeries series = new TimeSeries(name);
        for (int i = 0; i < timestamps.size(); i++) {
            if (maValues.get(i) != null) {
                series.add(new Second(timestamps.get(i)), maValues.get(i));
            }
        }
        dataset.addSeries(series);
    }

    public static JFreeChart createCombinedChart(DefaultHighLowDataset dataset, TimeSeriesCollection maDataset) {
        NumberAxis yAxis = new NumberAxis("Price");
        yAxis.setAutoRangeIncludesZero(false);
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer();
        XYPlot plot = new XYPlot(dataset, new DateAxis("Time"), yAxis, candlestickRenderer);
        XYLineAndShapeRenderer maRenderer = new XYLineAndShapeRenderer(true, false);
        plot.setDataset(1, maDataset);
        plot.setRenderer(1, maRenderer);
        maRenderer.setSeriesPaint(0, Color.ORANGE);
        maRenderer.setSeriesPaint(1, Color.RED);
        maRenderer.setSeriesPaint(2, Color.GREEN);
        return new JFreeChart("OKX BTC/USDT 5m K线 + MA", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    }
    private static void addMouseHoverListener(ChartPanel panel) {
        panel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {}

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                if (event.getEntity() instanceof XYItemEntity) { // Java 8 兼容
                    XYItemEntity entity = (XYItemEntity) event.getEntity();
                    int seriesIndex = entity.getSeriesIndex();
                    int itemIndex = entity.getItem();
                    infoLabel.setText("Series: " + seriesIndex + ", Item: " + itemIndex);
                }
            }
        });
    }
}
