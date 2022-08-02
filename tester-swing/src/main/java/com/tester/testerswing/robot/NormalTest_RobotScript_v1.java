package com.tester.testerswing.robot;

import java.awt.*;
import java.awt.event.InputEvent;


public class NormalTest_RobotScript_v1 {
    public static Robot r;

    static {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        NormalTest_RobotScript_v1 root = new NormalTest_RobotScript_v1();
        root.silotCV_engage();

    }

    public void demo(){
        r.delay(123);
        RobotHelper.move(555, 555, 589);
    }

    public void silotCV_engage() throws Exception {
        r.delay(123);
        RobotHelper.move(1728, 861, 589);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        r.delay(480);
        RobotHelper.move(1772, 871);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(41);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void silotCV_return() throws Exception {
        r.delay(123);
        RobotHelper.move(1728, 861, 589);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        r.delay(480);
        RobotHelper.move(1774, 918, 589);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(45);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }



}
