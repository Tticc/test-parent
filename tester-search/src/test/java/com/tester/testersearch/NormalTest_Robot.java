package com.tester.testersearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NormalTest_Robot {
    public static Robot r;

    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_selectWindow() throws Exception {
        r.mouseMove(1731, 1056);
        r.delay(1000);
//        r.mouseMove(1792,1022);
//        r.delay(1000);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(200);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    @Test
    public void test_robot() throws Exception {
        Point point = MouseInfo.getPointerInfo().getLocation();
        double x = point.getX();
        System.out.println("x = " + x);
        double y = point.getY();
        System.out.println("y = " + y);
        r.mouseMove(785, 437);
        TimeUnit.SECONDS.sleep(1);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        TimeUnit.SECONDS.sleep(1);
        r.mouseMove(866, 472);
        TimeUnit.SECONDS.sleep(1);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

}
