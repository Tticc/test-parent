package com.tester.testerswing.onTopReplica;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
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
        PointInfoDTO saiListPoint = new PointInfoDTO().setX(304).setY(99);
        PointInfoDTO silotListPoint = new PointInfoDTO().setX(362).setY(123);

        // 依次打开账号，将两个账号放到onTopReplica的界面选项首位。也就是上面的 saiListPoint 和 silotListPoint 两个点
        EventHandle_Main.openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
        RobotHelper.delay(200);
        EventHandle_Main.openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);

        // sai 截屏
        startOnToRepli1(saiListPoint);
        // 移动 sai 到右侧临时位置
        move_sai_first();

        // silot 截屏
        startOnToRepli1(silotListPoint);
        // 移动 silot 到最终位置
        move_silot_first();
        // 移动 sai 到最终位置
        move_sai_last();
        RobotHelper.delay(200);
        RobotHelper.move(555, 555);
    }

    private static void startOnToRepli1(PointInfoDTO account) {

        RobotHelper.move(89, 1056);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(126, 1003);
        RobotHelper.keyPress("OnTopReplica");
        RobotHelper.move(198, 372);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(264, 203);
        RobotHelper.delay(456);
        RobotHelper.mouseRightPress();
        RobotHelper.move(326, 367);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(475, 395);
        RobotHelper.mouseLeftPress();
        // 移动到起始位置
        RobotHelper.move(61, 55);
        RobotHelper.mouseRightPress();
        RobotHelper.move(136, 72);
        RobotHelper.mouseLeftPress();
        // 选择 a
        RobotHelper.move(account.getX(), account.getY());
        RobotHelper.mouseLeftPress();
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
        RobotHelper.move(785, 68);
        // 按下鼠标左键
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        // 移动到区域终止点
        RobotHelper.move(821, 301);
        // 弹起鼠标左键
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        // 区域选择完成
        RobotHelper.move(1252, 278);
        RobotHelper.mouseLeftPress();

//        // 拖动放缩分屏窗口
        RobotHelper.move(164,1004);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(112,708);
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

    public static void move_sai_first() {
        // 拖动1
        RobotHelper.move(61, 55);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(1670, 201);
        RobotHelper.delay(100);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(100);
    }

    public static void move_silot_first() {
        // 拖动1
        RobotHelper.move(61, 55);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(99, 57);
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
    }

    public static void move_sai_last() {
        // 拖动1
        RobotHelper.move(1670, 201);
        RobotHelper.r.mousePress(InputEvent.BUTTON1_MASK);
        RobotHelper.move(200, 63);
        RobotHelper.delay(500);
        RobotHelper.r.mouseRelease(InputEvent.BUTTON1_MASK);
        RobotHelper.delay(500);
    }

}
