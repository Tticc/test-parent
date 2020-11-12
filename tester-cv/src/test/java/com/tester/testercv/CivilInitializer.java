package com.tester.testercv;

import java.util.List;

/**
 * 初始化器
 */
public class CivilInitializer {
    /**
     * 文明总量
     */
    private int maxNum = 500;
    /**
     * 文明最短距离。ly
     */
    private int minDis = 5;
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
}
