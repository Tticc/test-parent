package com.tester.testerswing;

import com.tester.testerswing.robot.RobotHelper;

import java.awt.event.InputEvent;

/**
 * @Author 温昌营
 * @Date 2022-8-3 09:18:45
 */
public class NormalTest1 {

    public static void main(String[] args) {
        test_demo();
    }

    public static void test_demo() {
        // 移动到起始点，右键
        RobotHelper.move(40, 34);
        RobotHelper.mouseRightPress();
        // 移动并选择窗口菜单
        RobotHelper.move(103, 46);
        RobotHelper.mouseLeftPress();
        // 移动并选中窗口
        RobotHelper.move(287, 93);
        RobotHelper.mouseLeftPress();

        // 移动到起始点，右键
        RobotHelper.move(40, 34);
        RobotHelper.mouseRightPress();
        // 移动并选择 放缩 菜单
        RobotHelper.move(108, 160);
        RobotHelper.mouseLeftPress();
        // 放缩 一半
        RobotHelper.move(257, 201);
        RobotHelper.mouseLeftPress();


        // 移动到起始点，右键
        RobotHelper.move(40, 34);
        RobotHelper.mouseRightPress();
        // 选择区域
        RobotHelper.move(81, 88);
        RobotHelper.mouseLeftPress();

        // 移动到区域起始点
        RobotHelper.move(780, 140);
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        // 移动到区域终止点
        RobotHelper.move(908, 441);
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);

        // 区域选中完成
        RobotHelper.move(1189, 235);
        RobotHelper.mouseLeftPress();

        // 放缩
        RobotHelper.move(440, 1017);
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(281, 650);
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);


        // 移动到起始点，右键
        RobotHelper.move(40, 34);
        RobotHelper.mouseRightPress();
        // 解除锁定位置
        RobotHelper.move(99, 178);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(212, 182);
        RobotHelper.mouseLeftPress();

        // 放置到临时位置
        // 移动到起始点，右键
        RobotHelper.move(40, 34);
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(1665, 122);
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);

    }
}
