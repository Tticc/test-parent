package com.tester.testerswing;

import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;

import java.awt.*;

/**
 * @Author 温昌营
 * @Date 2022-8-3 09:18:45
 */
public class NormalTest {

    public static void main(String[] args) {
        Point point = RobotHelper.getPoint();
        for (int i = 0; i < 15; i++) {
            int x = GaussianHelper.getGaussianInt(1438, 1547);
            int y = GaussianHelper.getGaussianInt(34, 51);
            RobotHelper.move(x, y);
            RobotHelper.delay(1000);
            RobotHelper.move((int)point.getX(),(int)point.getY());
            RobotHelper.delay(1000);
        }
    }
}
