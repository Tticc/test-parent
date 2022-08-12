package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
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
        // 暂停 silot
        script.getSilot_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(false);
            script.getSilot_status().setText("false");
        });
        // 继续 silot
        script.getSilot_start().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(true);
            script.getSilot_status().setText("true");
        });


        // 设置 open 按钮事件
        JButton open_silot = script.getOpen_silot();
        open_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        // 设置 return 按钮事件
        JButton open_return_silot = script.getOpen_return_silot();
        open_return_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                returnOpe(EventHandle_Silot.silot_return_p1, EventHandle_Silot.silot_return_p2, EventHandle_Silot.silot_return_p3, EventHandle_Silot.silot_main_view_p1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 run 按钮事件
        JButton open_run_silot = script.getOpen_run_silot();
        open_run_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                runOpe(EventHandle_Silot.silot_align_p1, EventHandle_Silot.silot_align_p2, EventHandle_Silot.silot_align_p3, EventHandle_Silot.silot_main_view_p1);
                returnOpe(EventHandle_Silot.silot_return_p1, EventHandle_Silot.silot_return_p2, EventHandle_Silot.silot_return_p3, EventHandle_Silot.silot_main_view_p1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    // sai 操作处理
    private static void handle_sai(EasyScript_UI_Main script) {
        // 暂停 sai
        script.getSai_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedWarn(false);
            script.getSai_status().setText("false");
        });
        // 继续 sai
        script.getSai_start().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedWarn(true);
            script.getSai_status().setText("true");
        });

        // 设置 open 按钮事件
        JButton open_sai = script.getOpen_sai();
        open_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 return 按钮事件
        JButton open_return_sai = script.getOpen_return_sai();
        open_return_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                returnOpe(EventHandle_Sai.sai_return_p1, EventHandle_Sai.sai_return_p2, EventHandle_Sai.sai_return_p3, EventHandle_Sai.sai_main_view_p1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 run 按钮事件
        JButton open_run_sai = script.getOpen_run_sai();
        open_run_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                runOpe(EventHandle_Sai.sai_align_p1, EventHandle_Sai.sai_align_p2, EventHandle_Sai.sai_align_p3, EventHandle_Sai.sai_main_view_p1);
                RobotHelper.delay(500);
                returnOpe(EventHandle_Sai.sai_return_p1, EventHandle_Sai.sai_return_p2, EventHandle_Sai.sai_return_p3, EventHandle_Sai.sai_main_view_p1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void openOpe(GaussianPointInfoDTO p1, GaussianPointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 186);
        RobotHelper.mouseLeftPress();
    }

    private static void returnOpe(GaussianPointInfoDTO p1, GaussianPointInfoDTO p2, GaussianPointInfoDTO p3, GaussianPointInfoDTO mvP1) {
        RobotHelper.move(p1.getX(), p1.getY(), 213);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 237);
        RobotHelper.mouseRightPress();
        RobotHelper.move(p3.getX(), p3.getY(), 513);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(mvP1.getX(), mvP1.getY(), 517);
        RobotHelper.mouseLeftPress();
    }

    private static void runOpe(GaussianPointInfoDTO p1, GaussianPointInfoDTO p2, GaussianPointInfoDTO p3, GaussianPointInfoDTO mvP1) {
        RobotHelper.move(p1.getX(), p1.getY(), 331);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 386);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p3.getX(), p3.getY(), 627);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(100);
    }


}
