package com.tester.thirdparty.okx;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OKXCandlestickChart {
    private static JFrame frame;
    private static ChartPanel chartPanel;
    private static JLabel infoLabel;  // 显示蜡烛信息

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("OKX BTC/USDT 5m K线 + MA");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLayout(new BorderLayout());

            // 创建 K 线图
            JFreeChart chart = createChart();
            chartPanel = new ChartPanel(chart);

            // ✅ 启用拖动 & 缩放
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setDomainPannable(true);  // 允许 X 轴拖动
            plot.setRangePannable(true);   // 允许 Y 轴拖动
            chartPanel.setMouseWheelEnabled(true);  // 启用鼠标滚轮缩放

            frame.add(chartPanel, BorderLayout.CENTER);

            // 显示 K 线数据的标签
            infoLabel = new JLabel("鼠标移动到蜡烛上查看详细数据");
            frame.add(infoLabel, BorderLayout.SOUTH);

            // 添加鼠标事件监听
            addMouseHoverListener(chartPanel);

            frame.setVisible(true);

            // 每 5 分钟自动更新
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                JFreeChart updatedChart = createChart();
                SwingUtilities.invokeLater(() -> {
                    frame.remove(chartPanel);
                    chartPanel = new ChartPanel(updatedChart);

                    // ✅ 重新启用缩放 & 拖动
                    XYPlot updatedPlot = (XYPlot) updatedChart.getPlot();
                    updatedPlot.setDomainPannable(true);
                    updatedPlot.setRangePannable(true);
                    chartPanel.setMouseWheelEnabled(true);

                    frame.add(chartPanel, BorderLayout.CENTER);
                    addMouseHoverListener(chartPanel);  // 重新添加监听
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

        OKXCandlestickChart2.getOKXKlineData(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);

        // 计算 MA
        List<Double> ma5 = OKXCandlestickChart2.calculateMA(closePrices, 5);
        List<Double> ma10 = OKXCandlestickChart2.calculateMA(closePrices, 10);
        List<Double> ma20 = OKXCandlestickChart2.calculateMA(closePrices, 20);

        // 创建 K 线数据集
        DefaultHighLowDataset dataset = OKXCandlestickChart2.createCandlestickDataset(timestamps, openPrices, highPrices, lowPrices, closePrices, volumes);

        // 创建 MA 线数据集
        TimeSeriesCollection maDataset = new TimeSeriesCollection();
        OKXCandlestickChart2.addMAToDataset(maDataset, "MA5", timestamps, ma5);
        OKXCandlestickChart2.addMAToDataset(maDataset, "MA10", timestamps, ma10);
        OKXCandlestickChart2.addMAToDataset(maDataset, "MA20", timestamps, ma20);

        // 创建 K 线 + MA 线的图表
        return OKXCandlestickChart2.createCombinedChart(dataset, maDataset);
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
