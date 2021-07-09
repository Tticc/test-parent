package com.tester.testercv.rateCount;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class RateCount_OptimizeTest {

    /**
     * 平均每次目标数量。如果为null，那么取概率的均值。
     **/
    private Integer targetNumPer = null;
    /**
     * 尝试次数。（抽奖次数）
     **/
    private Integer tryNum = 14;
    /**
     * 当次数大于asyncTryNum的时候开始使用异步。通过asyncTryNum和asyncLevel来限制异步线程的工作量
     **/
    private Integer asyncTryNum = 13 - 1; //Integer.MAX_VALUE;
    /**     * 第几层开始作为异步任务分发出去。默认是1，从第二轮循环开始异步。
     **/
    private int asyncLevel = 1;

    /**
     * <ol>
     *     <li>1 = 0.227</li>
     *     <li>2 = 0.17057100000000003</li>
     * </ol>
     *
     * @Date 16:44 2021/7/1
     * @Author 温昌营
     **/
    @Test
    public void test_rate_cal() {
        System.out.println("********************************* verify area start *********************************");
        Map<Integer, Double> rateMap = getRateMap();
        verifyRateMap(rateMap);
        System.out.println("********************************* verify area end *********************************");
        System.out.println();

        Map<Integer, Double> points = new HashMap<>();
        doProcess(points);
        System.out.println(JSON.toJSONString(points));

    }

    private void doProcess(Map<Integer, Double> points) {
        Integer times = tryNum;
        if (times < 1) {
            return;
        }
        Map<Integer, Double> rateMap = getRateMap();
        for (int i = 1; i <= times; i++) {
            // 目标值：默认为平均值可能值，以此，计算特定次数下，获得平均值以上价值数量的概率
            Integer target = null == targetNumPer ? getAverage(rateMap).intValue() * i : targetNumPer * i;
            // 初始化数据，异步task需要而做的改造
            PersistData initData = getInitData(i, target, 0.0);
            StopWatch sw = new StopWatch();
            sw.start();
            // 开始处理
            doProcessSub(initData, 0, 0, 1.0);
            sw.stop();
            long totalTimeMillis = sw.getTotalTimeMillis();
            // 尝试次数 - 目标数量 - 耗时 - 当前时间
            System.out.println("tryNum: " + i + ", target: " + target + ", time cost: " + totalTimeMillis / 1000.0 + ", now: " + new Date());
            // 命中数量
            System.out.println("hit size: " + initData.getSize());
            // 总概率（所有命中的概率之和就是总概率）
            System.out.println("rate: " + initData.getTotalRate());

            // 将 次数-概率 组返回
            points.put(i,initData.getTotalRate());
            initData.setSize(0L);
            initData.setTotalRate(0.0);
            System.out.println();
        }
    }

    /**
     * @param initData 全局数据
     * @param curLevel 临时level
     * @param curNum   临时价值数量
     * @param curRate  临时概率
     * @return void
     * @Date 18:08 2021/7/2
     * @Author 温昌营
     **/
    private void doProcessSub(
            PersistData initData,
            Integer curLevel,
            Integer curNum,
            Double curRate) {
        // 如果当前价值数量大于等于目标价值数量，返回（剪枝算法）
        if (curNum >= initData.getTargetNum()) {
            // 概率累加
            initData.setTotalRate(initData.getTotalRate() + curRate);
            // 命中次数加1
            initData.setSize(initData.getSize() + 1);
            return;
        }
        // 到达最底层，返回
        if (curLevel >= initData.getTargetLevel()) {
            return;
        }
        // proxy，用来做异步、同步的选择
        doProcessSubProxy(initData, curLevel, curNum, curRate);
    }

    private void doProcessSubProxy(PersistData initData,
                                   Integer curLevel,
                                   Integer curNum,
                                   Double curRate) {
        if (initData.getTargetLevel() > asyncTryNum && curLevel == asyncLevel) {
            // async
            doAsync(initData, curLevel, curNum, curRate);
        } else {
            // sync
            doSync(initData, curLevel, curNum, curRate);
        }
    }

    // 同步处理
    private void doSync(PersistData initData,
                        Integer curLevel,
                        Integer curNum,
                        Double curRate) {
        for (int i = 0; i < initData.getEntryList().size(); i++) {
            Map.Entry<Integer, Double> entry = initData.getEntryList().get(i);
            doProcessSub(initData, curLevel + 1, curNum + entry.getKey(), curRate * entry.getValue());
        }
    }

    // 异步处理
    private void doAsync(PersistData initData,
                         Integer curLevel,
                         Integer curNum,
                         Double curRate) {

        // futureList，保存异步任务返回
        List<Future<PersistData>> futureList = new ArrayList<>();
        for (int i = 0; i < initData.getEntryList().size(); i++) {
            Map.Entry<Integer, Double> entry = initData.getEntryList().get(i);
            // 复制一份
            PersistData copy = initData.copy();
            // 设置总概率0.0，后续做累加处理
            copy.setTotalRate(0.0);
            // size设置为0，后续做累加处理
            copy.setSize(0L);
            // 异步处理（启用本地线程提升不大，可以考虑额外机器）
            Future<PersistData> persistDataFuture = RateCount_Optimize_AsyncTaskTest.doAsync(() -> {
                doProcessSub(copy, curLevel + 1, curNum + entry.getKey(), curRate * entry.getValue());
                return copy;
            });
            futureList.add(persistDataFuture);
        }
        int i = 0;
        Double rate = 0.0;
        Long size = initData.getSize();
        // 处理featureList，获取返回数据。
        do {
            Future<PersistData> persistDataFuture = futureList.get(i);
            PersistData persistData = null;
            try {
                // 获取数据
                persistData = persistDataFuture.get(5, TimeUnit.SECONDS);
                rate += persistData.getTotalRate();
                size += persistData.getSize();
                ++i;
            } catch (Exception e) {
                // 超时或其他异常则继续do while循环
//                System.out.println(e.getMessage() + e.toString());
            }
        } while (i < futureList.size());
        // 设置、累加
        initData.setTotalRate(initData.getTotalRate() + rate);
        // 设置命中次数
        initData.setSize(size);
    }


    /**
     * 获取平均价值数量
     *
     * @param map
     * @return java.lang.Double
     * @Date 18:22 2021/7/2
     * @Author 温昌营
     **/
    private Double getAverage(Map<Integer, Double> map) {
        Assert.notEmpty(map, "map should not empty");
        double res = 0d;
        Set<Map.Entry<Integer, Double>> entries = map.entrySet();
        for (Map.Entry<Integer, Double> entry : entries) {
            res += entry.getKey() * entry.getValue();
        }
        return res;
    }


    /**
     * 获取 价值数量-概率。正序、逆序排序
     *
     * @param
     * @return java.util.List<java.util.Map.Entry < java.lang.Integer, java.lang.Double>>
     * @Date 18:22 2021/7/2
     * @Author 温昌营
     **/
    private List<Map.Entry<Integer, Double>> getEntryList() {
        Map<Integer, Double> map = getRateMap();
        Set<Map.Entry<Integer, Double>> entries = map.entrySet();
        List<Map.Entry<Integer, Double>> collect = entries.stream()
                .sorted((a, b) -> b.getKey().compareTo(a.getKey())) // 逆序排序
//                .sorted((a, b) -> a.getKey().compareTo(b.getKey())) // 正序排序
                .collect(Collectors.toList());
        return collect;
    }

    private PersistData getInitData(Integer totalLevel, Integer targetNum, Double totalRate) {
        PersistData persistData = new PersistData();
        return persistData.setTargetLevel(totalLevel)
                .setTargetNum(targetNum)
                .setTotalRate(totalRate)
                .setSize(0L)
                .setEntryList(getEntryList());
    }

    @Data
    @Accessors(chain = true)
    class PersistData {
        private Integer targetLevel;
        private Integer targetNum;
        private Double totalRate;
        private Long size;
        private List<Map.Entry<Integer, Double>> entryList;

        public PersistData copy() {
            return new PersistData()
                    .setTargetLevel(new Integer(this.getTargetLevel().intValue()))
                    .setTargetNum(new Integer(this.getTargetNum().intValue()))
                    .setTotalRate(new Double(this.getTotalRate().doubleValue()))
                    .setSize(new Long(this.getSize().longValue()))
                    .setEntryList(this.getEntryList());
        }
    }

    private void verifyRateMap(Map<Integer, Double> rateMap){
        if (CollectionUtils.isEmpty(rateMap)) {
            System.out.println("rateMap is empty!!!");
            return;
        }
        Set<Map.Entry<Integer, Double>> entries = rateMap.entrySet();
        System.out.println("map size: " + entries.size());
        System.out.println("average: " + getAverage(rateMap).intValue());
        Double reduce = entries.stream().map(e -> e.getValue()).reduce(0.0, (a, b) -> a + b);
        System.out.println("total rate: " + reduce);
    }

    /**
     * 初始化 价值数量-概率 列表
     *
     * @param
     * @return java.util.Map<java.lang.Integer, java.lang.Double>
     * @Date 18:23 2021/7/2
     * @Author 温昌营
     **/
    private Map<Integer, Double> getRateMap() {
        Map<Integer, Double> rateMap = new HashMap<>();
        rateMap.put(5, 0.28d);
        rateMap.put(10, 0.283d);
        rateMap.put(50, 0.21d);
        rateMap.put(100, 0.15d);
        rateMap.put(300, 0.04d);
        rateMap.put(500, 0.02d);
        rateMap.put(1000, 0.01d);
        rateMap.put(2000, 0.005d);
        rateMap.put(5000, 0.002d);

//        Map<Integer, Double> rateMap = new HashMap<>();
//        rateMap.put(5, 0.38d);
//        rateMap.put(15, 0.4222d);
//        rateMap.put(24, 0.1d);
//        rateMap.put(35, 0.035d);
//        rateMap.put(55, 0.021d);
//        rateMap.put(75, 0.009d);
//        rateMap.put(150, 0.008d);
//        rateMap.put(250, 0.007d);
//        rateMap.put(350, 0.006d);
//        rateMap.put(450, 0.005d);
//        rateMap.put(550, 0.004d);
//        rateMap.put(650, 0.001d);
//        rateMap.put(750, 0.0005d);
//        rateMap.put(850, 0.0004d);
//        rateMap.put(950, 0.0003d);
//        rateMap.put(2000, 0.0002d);
//        rateMap.put(4000, 0.0002d);
//        rateMap.put(5000, 0.0002d);
        return rateMap;
    }

}
