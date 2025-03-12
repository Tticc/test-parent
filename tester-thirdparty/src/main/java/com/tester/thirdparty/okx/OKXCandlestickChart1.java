package com.tester.thirdparty.okx;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OKXCandlestickChart1 {
    private static JFrame frame;
    private static ChartPanel chartPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("OKX BTC/USDT 5m K线图 + MA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            // 初次获取数据并绘制
            JFreeChart chart = createChart();
            chartPanel = new ChartPanel(chart);
            frame.add(chartPanel);
            frame.setVisible(true);

            // 每 5 分钟更新一次
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                JFreeChart updatedChart = createChart();
                SwingUtilities.invokeLater(() -> {
                    frame.remove(chartPanel);
                    chartPanel = new ChartPanel(updatedChart);
                    frame.add(chartPanel);
                    frame.revalidate();
                    frame.repaint();
                });
            }, 5, 1, TimeUnit.SECONDS);
        });
    }

    private static JFreeChart createChart() {
        List<Date> timestamps = new ArrayList<>();
        List<Double> openPrices = new ArrayList<>();
        List<Double> highPrices = new ArrayList<>();
        List<Double> lowPrices = new ArrayList<>();
        List<Double> closePrices = new ArrayList<>();
        List<Double> volumes = new ArrayList<>();

        getOKXKlineData(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);

        // 计算 MA
        List<Double> ma5 = calculateMA(closePrices, 5);
        List<Double> ma10 = calculateMA(closePrices, 10);
        List<Double> ma20 = calculateMA(closePrices, 20);

        // 创建蜡烛图数据集
        DefaultHighLowDataset dataset = createCandlestickDataset(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);

        // 创建 K 线图
        JFreeChart chart = createCandlestickChart(dataset, timestamps, closePrices, ma5, ma10, ma20);
        return chart;
    }

    private static void getOKXKlineData(List<Date> timestamps, List<Double> openPrices, List<Double> highPrices, List<Double> lowPrices, List<Double> closePrices, List<Double> volumes) {
        String apiUrl = "https://www.okx.com/api/v5/market/candles?instId=BTC-USDT&bar=5m&limit=100";

        try {
            String klineData = OKXPositionQuery.getKlineData();

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

                timestamps.add(new Date(timestamp));
                openPrices.add(openPrice);
                highPrices.add(highPrice);
                lowPrices.add(lowPrice);
                closePrices.add(closePrice);
                volumes.add(volume);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private static DefaultHighLowDataset createCandlestickDataset(List<Date> timestamps, List<Double> open, List<Double> high, List<Double> low, List<Double> close, List<Double> volume) {
        int size = timestamps.size();
        Date[] dateArr = timestamps.toArray(new Date[0]);
        double[] openArr = open.stream().mapToDouble(Double::doubleValue).toArray();
        double[] highArr = high.stream().mapToDouble(Double::doubleValue).toArray();
        double[] lowArr = low.stream().mapToDouble(Double::doubleValue).toArray();
        double[] closeArr = close.stream().mapToDouble(Double::doubleValue).toArray();
        double[] volumeArr = volume.stream().mapToDouble(Double::doubleValue).toArray();

        return new DefaultHighLowDataset("Candlestick", dateArr, highArr, lowArr, openArr, closeArr, volumeArr);
    }

    private static JFreeChart createCandlestickChart(DefaultHighLowDataset dataset, List<Date> timestamps, List<Double> closePrices, List<Double> ma5, List<Double> ma10, List<Double> ma20) {
        NumberAxis yAxis = new NumberAxis("Price");
        yAxis.setAutoRangeIncludesZero(false);
        CandlestickRenderer renderer = new CandlestickRenderer();
        XYPlot candlestickPlot = new XYPlot(dataset, null, yAxis, renderer);

        TimeSeriesCollection maDataset = new TimeSeriesCollection();
        addMAToDataset(maDataset, "MA5", timestamps, ma5);
        addMAToDataset(maDataset, "MA10", timestamps, ma10);
        addMAToDataset(maDataset, "MA20", timestamps, ma20);

        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        XYPlot maPlot = new XYPlot(maDataset, null, yAxis, lineRenderer);

        CombinedDomainXYPlot combinedPlot = new CombinedDomainXYPlot(new DateAxis("Time"));
        combinedPlot.add(candlestickPlot);
        combinedPlot.add(maPlot);

        return new JFreeChart("OKX BTC/USDT 5m K线 + MA", JFreeChart.DEFAULT_TITLE_FONT, combinedPlot, true);
    }

    private static void addMAToDataset(TimeSeriesCollection dataset, String name, List<Date> timestamps, List<Double> maValues) {
        TimeSeries series = new TimeSeries(name);
        for (int i = 0; i < timestamps.size(); i++) {
            if (maValues.get(i) != null) {
                series.add(new org.jfree.data.time.Second(timestamps.get(i)), maValues.get(i));
            }
        }
        dataset.addSeries(series);
    }
}
