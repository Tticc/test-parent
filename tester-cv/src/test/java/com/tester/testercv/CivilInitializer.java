package com.tester.testercv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 初始化器
 */
public class CivilInitializer {

    public static Random random = new Random();
    /**
     * 文明总量
     */
    private int maxNum = 20;
    /**
     * 宇宙长度(长、宽、高)
     */
    private int universe = 50000;
    /**
     * 文明最短距离。ly
     */
    private int minDis = 4;
    /**
     * 转化率。指消灭其他文明后的收益率
     */
    private float transRate = 0.4f;

    /**
     * 存活
     */
    private List<Civil> alive;
    /**
     * 消失
     */
    private List<Civil> death;


    public static void main(String[] args) throws InterruptedException {
        CivilInitializer civilInitializer = new CivilInitializer();
        civilInitializer.init();
    }
    public void init() throws InterruptedException {
        AtomicInteger integer = new AtomicInteger(0);
        this.alive = new ArrayList<>();
        this.death = new ArrayList<>();
        for (int i = 0; i < maxNum; i++) {
            int x = random.nextInt(universe);
            int y = random.nextInt(universe);
            int z = random.nextInt(universe);
            int[] newLandmark;
            boolean b;
            do{
                newLandmark = new int[]{x,y,z};
                b = checkLength(this.alive, newLandmark);
            }while (!b);
            Civil civil = new Civil();
            civil.setSpacePoint(newLandmark)
                    .setId(integer.getAndIncrement())
                    .setName("civil_"+civil.getId())
                    .setRadius(0);
            // 开始成长
            civil.growth();
            this.alive.add(civil);
        }
        CivilHelper.checker(alive,death);
    }

    private boolean checkLength(List<Civil> alive, int[] newLandmark) {
        for (Civil civil : alive) {
            int[] landmark = civil.getSpacePoint();
            int i = CivilHelper.calDis(landmark, newLandmark);
            if(i < minDis){
                return false;
            }
        }
        return true;
    }
}
