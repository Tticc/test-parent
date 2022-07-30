package com.tester.testerswing.robot;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class NormalTest_GetPoint {

    public static void main(String[] args) throws Exception {
        TimeUnit.SECONDS.sleep(2);
        Point point = MouseInfo.getPointerInfo().getLocation();
        double x = point.getX();
        System.out.println("x = " + x);
        double y = point.getY();
        System.out.println("y = " + y);
    }
}

//        x = 1774.0
//        y = 918.0

