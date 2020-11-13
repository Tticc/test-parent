package com.tester.testercv;


import lombok.Data;
import lombok.experimental.Accessors;


/**
 * civil
 */
@Data
@Accessors(chain = true)
public class Civil {
    /**
     * 清理基因。默认无
     */
    private boolean cleaner = false;
    /**
     * 隐藏基因。默认无
     */
    private boolean hider = false;

    /**
     * 年龄
     */
    private int age = 1;

    /**
     * 攻击力
     */
    private int attack = 0;
    /**
     * 防御力
     */
    private int defence = 0;
    /**
     * 血量
     */
    private int hp = 0;
    /**
     * 航行半径
     */
    private int radius = 0;



    // 坐标。需要初始化
    private int[] spacePoint;

    /**
     * id
     */
    private long id;
    /**
     * 名称
     */
    private String name;



    public void growth(){
        CivilHelper.civilGrowth(this);
    }
}
