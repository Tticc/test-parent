package com.tester.testerswing.robot;

import com.tester.testerswing.gaussian.GaussianHelper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;


public class RobotHelper {
    public static Robot r;
    public static Random random = new Random();
    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        demo();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(40)+35);
        }

    }

    public static Point getPoint(){
        return MouseInfo.getPointerInfo().getLocation();
    }


    public static void mouseRightPress(){
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(GaussianHelper.getGaussianInt(40)+35);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    public static void mouseLeftPress(){
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(GaussianHelper.getGaussianInt(40)+35);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void delay(int ms){
        r.delay(ms);
    }

    public static void move(int x, int y) {
        move(x, y, 85);
    }

    public static void move(int x, int y, int delayTimes) {
        r.delay(delayTimes);
        for (int i = 0; i < 5; i++) {
            r.mouseMove(x, y);
            r.delay(20);
        }
        r.delay(57);
    }




    private static void demo(){
        r.delay(123);
        move(555, 555, 589);
    }

}
