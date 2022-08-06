package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;

/**
 * main 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Main {


    public static void handle_main(EasyScript_UI_Main script) {
        handle_silot(script);
        handle_sai(script);
    }

    // silot 操作处理
    private static void handle_silot(EasyScript_UI_Main script) {
        // 设置按钮事件
        JButton open_silot = script.getOpen_silot();
        open_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_return_silot = script.getOpen_return_silot();
        open_return_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                returnOpe(EventHandle_Silot.silot_return_p1, EventHandle_Silot.silot_return_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_run_silot = script.getOpen_run_silot();
        open_run_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                runOpe(EventHandle_Silot.silot_align_p1, EventHandle_Silot.silot_align_p2, EventHandle_Silot.silot_align_p3, EventHandle_Silot.silot_main_view_p1);
                returnOpe(EventHandle_Silot.silot_return_p1, EventHandle_Silot.silot_return_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    // silot 操作处理
    private static void handle_sai(EasyScript_UI_Main script) {
        // 设置按钮事件
        JButton open_sai = script.getOpen_sai();
        open_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_return_sai = script.getOpen_return_sai();
        open_return_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                returnOpe(EventHandle_Sai.sai_return_p1, EventHandle_Sai.sai_return_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton open_run_sai = script.getOpen_run_sai();
        open_run_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                runOpe(EventHandle_Sai.sai_align_p1, EventHandle_Sai.sai_align_p2, EventHandle_Sai.sai_align_p3, EventHandle_Sai.sai_main_view_p1);
                RobotHelper.delay(500);
                returnOpe(EventHandle_Sai.sai_return_p1, EventHandle_Sai.sai_return_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private static void openOpe(PointInfoDTO p1, PointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 186);
        RobotHelper.mouseLeftPress();
    }

    private static void returnOpe(PointInfoDTO p1, PointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), 237);
        RobotHelper.mouseRightPress();
        RobotHelper.move(p2.getX(), p2.getY(), 513);
        RobotHelper.mouseLeftPress();
    }

    private static void runOpe(PointInfoDTO p1, PointInfoDTO p2, PointInfoDTO p3, PointInfoDTO mvP1) {
        RobotHelper.move(p1.getX(), p1.getY(), 331);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 386);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p3.getX(), p3.getY(), 417);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(500);
        RobotHelper.move(mvP1.getX(), mvP1.getY(), 117);
        RobotHelper.mouseLeftPress();
    }


}
