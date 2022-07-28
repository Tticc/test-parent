package com.tester.testersearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;


@Slf4j
public class NormalTest_Robot1 {

    public static void main(String[] args) {
        Point point = MouseInfo.getPointerInfo().getLocation();
        double x = point.getX();
        System.out.println("x = " + x);
        double y = point.getY();
        System.out.println("y = " + y);
    }

    @Test
    public void tess() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        double x = point.getX();
        System.out.println("x = " + x);
        double y = point.getY();
        System.out.println("y = " + y);
    }

}
