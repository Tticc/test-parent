package com.tester.testersearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NormalTest_Robot_v1 {
    public static Robot r;

    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        NormalTest_Robot_v1 root = new NormalTest_Robot_v1();
        root.silotCV_engage();

    }

    public void silotCV_engage() throws Exception {
        r.delay(123);
        move(1728, 861, 589);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        r.delay(480);
        move(1772, 871);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(41);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void silotCV_return() throws Exception {
        r.delay(123);
        move(1728, 861, 589);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        r.delay(480);
        move(1774, 918, 589);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }




    public static void move(int x, int y) {
        move(x,y,485);
    }

    public static void move(int x, int y, int delayTimes) {
        for (int i = 0; i < 5; i++) {
            r.mouseMove(x, y);
        }
        r.delay(delayTimes);
    }

}
