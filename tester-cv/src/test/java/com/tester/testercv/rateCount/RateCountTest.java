package com.tester.testercv.rateCount;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;

public class RateCountTest {
    private Double rate = 0.0;
    private Long size = 0L;


    /**
     * <ol>
     *     <li>1 = 1 - 0.77</li>
     *     <li>2 = 1 - 0.8239</li>
     * </ol>
     *
     * @Date 16:44 2021/7/1
     * @Author 温昌营
     **/
    @Test
    public void test_rate_cal() {
        StopWatch sw = new StopWatch();
        sw.start();
        doProcess();
        sw.stop();
        System.out.println(sw.getTotalTimeMillis()/1000.0);

    }

    private void doProcess(){
        Integer times = 11;
        if(times < 1){
            return;
        }
        Map<Integer, Double> rateMap = getRateMap();
        for (int i = 1; i <= times; i++) {
            Integer target = getAverage(rateMap).intValue()*i;
            RateNode rootNode = initParent(target);
            doProcessSub(rootNode, i);
            System.out.println("times: " + i + ", target: "+ target);
            System.out.println("size: " + size);
            System.out.println("rate: " + rate);
            size = 0L;
            rate = 0.0;
            System.out.println();
        }
    }
    private void doProcessSub(RateNode parentNode, Integer level){
        if(parentNode.getCurrentNum() >= parentNode.getTargetNum()){
//            targetLeafNode.add(parentNode);
            rate+=parentNode.getCurrentRate();
            ++size;
            return;
        }
        if(parentNode.getLevel() >= level){
            return;
        }
        if(CollectionUtils.isEmpty(parentNode.getList())){
            generateRateNodeByMap(parentNode);
        }
        List<RateNode> list = parentNode.getList();
        for (int i = 0; i < list.size(); i++) {
            RateNode node = list.get(i);
            node.setCurrentNum(node.getCurrentNum()+node.getNum());
            node.setCurrentRate(node.getRate()*parentNode.getCurrentRate());
            doProcessSub(node,level);
            list.set(i,null);
        }


    }

    private Double getAverage(Map<Integer, Double> map) {
        Assert.notEmpty(map, "map should not empty");
        double res = 0d;
        Set<Map.Entry<Integer, Double>> entries = map.entrySet();
        for (Map.Entry<Integer, Double> entry : entries) {
            res += entry.getKey() * entry.getValue();
        }
        return res;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class RateNode{
        Integer num; // num
        Double rate; // 概率，每次一num值出现的概率
        List<RateNode> list; // 所有的下一层


        Integer level; // 层级、也就是次数
        Integer targetNum; // 目标值
        Integer currentNum; // 当前值。当前值为整个链路num之和
        Double currentRate; // 走到当前节点的概率

        @JSONField(serialize = false,deserialize = false)
        RateNode parent;

        @Override
        public String toString(){
            return JSONObject.toJSONString(this);
        }
    }

    private void generateRateNodeByMap(RateNode parent) {
        Map<Integer, Double> map = getRateMap();
        Set<Map.Entry<Integer, Double>> entries = map.entrySet();
        List<RateNode> collect = entries.stream()
                .sorted((a, b) -> b.getKey().compareTo(a.getKey())) // 逆序排序
                .map(e -> initByParent(parent,e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        parent.setList(collect);
    }

    private RateNode initParent(Integer target){
        return new RateNode(0, 0.0, new ArrayList<>(), 0,target,0,1.0,null);
    }

    private RateNode initByParent(RateNode parent, Integer num, Double rate){
        return new RateNode(num,rate,new ArrayList<>(),parent.getLevel()+1,parent.getTargetNum(),parent.getCurrentNum(),0.0,parent);
    }


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
        return rateMap;
    }

}
