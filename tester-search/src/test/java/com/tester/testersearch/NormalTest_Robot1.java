package com.tester.testersearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NormalTest_Robot1 {

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

