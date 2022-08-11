package com.tester.testerswing;

import java.util.Random;

/**
 * @Author 温昌营
 * @Date 2022-8-3 09:18:45
 */
public class NormalTest1 {

    public static void main(String[] args) {
        boolean b = checkReturnTrue();
        System.out.println(b);
    }

    public static boolean checkReturnTrue() {
        return randomTrue() || randomTrue() || randomTrue();
    }

    public static boolean randomTrue() {
        Random random = new Random();
        int i = random.nextInt(10);
        System.out.println("i = " + i);
        if (i > 5) {
            return true;
        }
        return false;
    }
}
