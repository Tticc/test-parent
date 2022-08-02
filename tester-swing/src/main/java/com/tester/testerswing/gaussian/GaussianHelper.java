package com.tester.testerswing.gaussian;

import java.util.Random;

/**
 * @Author 温昌营
 * @Date 2022-8-2 18:07:36
 */
public class GaussianHelper {
    private static Random rand = new Random();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int gaussianInt = GaussianHelper.getGaussianInt(40);
            System.out.println(gaussianInt + 35);
        }
    }

    public static int getGaussianInt(int range) {
        return (int) getGaussianDouble(range);
    }


    public static double getGaussianDouble(int range) {
        return ((rand.nextGaussian() + 5.0) / 10) * range;
    }
}
