package com.tester.testerswing.onTopReplica;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.eventHandler.EventHandle_Colos;
import com.tester.testerswing.swing.eventHandler.EventHandle_Main;
import com.tester.testerswing.swing.eventHandler.EventHandle_Sai;
import com.tester.testerswing.swing.eventHandler.EventHandle_Silot;

import java.awt.event.InputEvent;

/**
 * 启动屏幕监控
 */
public class StartOnTopReplica {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        // onTopReplica 窗口选择点
        PointInfoDTO silotListPoint = new PointInfoDTO().setX(304).setY(99);
        PointInfoDTO saiListPoint = new PointInfoDTO().setX(362).setY(123);
        PointInfoDTO colosListPoint = new PointInfoDTO().setX(361).setY(152);

        // 依次打开账号，将两个账号放到onTopReplica的界面选项首位。也就是上面的 saiListPoint 和 silotListPoint 两个点

        EventHandle_Main.openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
        RobotHelper.delay(200);
        EventHandle_Main.openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
        RobotHelper.delay(200);
        EventHandle_Main.openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);

        // silot 截屏
        startOnToRepli1(silotListPoint);
        move_silot_first();
//
//        // sai 截屏
        startOnToRepli1(saiListPoint);
        move_silot_first2();

//        // colos 截屏
        startOnToRepli1(colosListPoint);
        move_silot_first3();

        RobotHelper.delay(200);
        RobotHelper.move(555, 555);
    }

    private static void startOnToRepli1(PointInfoDTO account) {
        select(account);
        // 移动到起始位置
        RobotHelper.move(61, 55);
        RobotHelper.mouseRightPress();
        RobotHelper.move(162, 202);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(322, 254);
        RobotHelper.mouseLeftPress();


        // 移动到起始位置
        RobotHelper.move(61, 55);
        RobotHelper.mouseRightPress();
        RobotHelper.move(132, 123);
        RobotHelper.mouseLeftPress();

        // 移动到区域起始点
        RobotHelper.move(790, 68);
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        // 移动到区域终止点
        RobotHelper.move(815, 301);
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        // 区域选择完成
        RobotHelper.move(1252, 278);
        RobotHelper.mouseLeftPress();

//        // 拖动放缩分屏窗口
        RobotHelper.move(113, 1004);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(78, 777);
        RobotHelper.delay(1500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);

        // 解除锁定
        RobotHelper.move(61, 55);
        RobotHelper.mouseRightPress();
        RobotHelper.move(127, 224);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(291, 229);
        RobotHelper.mouseLeftPress();

    }

    public static void move_silot_first() {
        // 拖动1
        RobotHelper.move(61, 55);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(98, 57);
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
    }

    public static void move_silot_first2() {
        // 拖动1
        RobotHelper.move(61, 55);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(175, 58);
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
    }

    public static void move_silot_first3() {
        // 拖动1
        RobotHelper.move(61, 55);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(251, 58);
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
    }


    public static void select(PointInfoDTO account) {

        RobotHelper.move(89, 1056);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(126, 1003);
        RobotHelper.keyPress("OnTopReplica");
        RobotHelper.move(198, 372);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(357, 297);
        RobotHelper.delay(456);
        RobotHelper.mouseRightPress();
        RobotHelper.move(440, 468);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(589, 495);
        RobotHelper.mouseLeftPress();
        // 移动到起始位置
        RobotHelper.move(61, 55);
        RobotHelper.mouseRightPress();
        // 选择窗口
        RobotHelper.move(136, 72);
        RobotHelper.mouseLeftPress();
        // 选择 a
        RobotHelper.move(362, 123);
        RobotHelper.move(account.getX(), account.getY());
        RobotHelper.mouseLeftPress();
    }
}
