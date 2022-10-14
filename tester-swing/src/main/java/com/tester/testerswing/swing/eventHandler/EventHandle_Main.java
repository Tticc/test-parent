package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
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
        handle_colos(script);
    }

    // silot 操作处理
    private static void handle_silot(EasyScript_UI_Main script) {
        // 暂停 silot
        script.getSilot_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(false);
            script.getSilot_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 silot
        script.getSilot_start().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(true);
            script.getSilot_status().setText("true");
        });

        // 暂停 silot info
        script.getSilot_info_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedInfo(false);
            script.getSilot_info_status().setText("false");
        });
        // 继续 silot info
        script.getSilot_info_start().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedInfo(true);
            script.getSilot_info_status().setText("true");
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
            script.getWarn_status().setText("false");
        });
        // 继续 sai
        script.getSai_start().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedWarn(true);
            script.getSai_status().setText("true");
        });

        // 暂停 sai info
        script.getSai_info_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedInfo(false);
            script.getSai_info_status().setText("false");
        });
        // 继续 sai info
        script.getSai_info_start().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedInfo(true);
            script.getSai_info_status().setText("true");
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

    // colos 操作处理
    private static void handle_colos(EasyScript_UI_Main script) {
        // 暂停 colos
        script.getColos_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(2).setNeedWarn(false);
            script.getColos_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 colos
        script.getColos_start().addActionListener((e) -> {
            script.getAccountInfoList().get(2).setNeedWarn(true);
            script.getColos_status().setText("true");
        });

        // 暂停 colos info
        script.getColos_info_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(2).setNeedInfo(false);
            script.getColos_info_status().setText("false");
        });
        // 继续 colos info
        script.getColos_info_start().addActionListener((e) -> {
            script.getAccountInfoList().get(2).setNeedInfo(true);
            script.getColos_info_status().setText("true");
        });

        // 设置 open 按钮事件
        JButton open_colos = script.getOpen_colos();
        open_colos.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 return 按钮事件
        JButton open_return_colos = script.getOpen_return_colos();
        open_return_colos.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                returnOpe(EventHandle_Colos.colos_return_p1, EventHandle_Colos.colos_return_p2, EventHandle_Colos.colos_return_p3, EventHandle_Colos.colos_main_view_p1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 run 按钮事件
        JButton open_run_colos = script.getOpen_run_colos();
        open_run_colos.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                runOpe(EventHandle_Colos.colos_align_p1, EventHandle_Colos.colos_align_p2, EventHandle_Colos.colos_align_p3, EventHandle_Colos.colos_main_view_p1);
                RobotHelper.delay(500);
                returnOpe(EventHandle_Colos.colos_return_p1, EventHandle_Colos.colos_return_p2, EventHandle_Colos.colos_return_p3, EventHandle_Colos.colos_main_view_p1);
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

    public static void returnOpe(GaussianPointInfoDTO p1, GaussianPointInfoDTO p2, GaussianPointInfoDTO p3, GaussianPointInfoDTO mvP1) {
        RobotHelper.move(p1.getX(), p1.getY(), 213);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 237);
        RobotHelper.mouseRightPress();
        RobotHelper.move(p3.getX(), p3.getY(), 513);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(mvP1.getX(), mvP1.getY(), 517);
        RobotHelper.mouseLeftPress();
    }

    public static void runOpe(GaussianPointInfoDTO p1, GaussianPointInfoDTO p2, GaussianPointInfoDTO p3, GaussianPointInfoDTO mvP1) {
        RobotHelper.move(p1.getX(), p1.getY(), 331);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 386);
        RobotHelper.mouseLeftPress();
        // 点击三次
        RobotHelper.move(p3.getX(), p3.getY(), 627);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(70);
    }

    public static void quickRunOpe(GaussianPointInfoDTO p1,
                                   GaussianPointInfoDTO p2,
                                   GaussianPointInfoDTO p3,
                                   GaussianPointInfoDTO p4,
                                   GaussianPointInfoDTO p5,
                                   GaussianPointInfoDTO p6,
                                   GaussianPointInfoDTO p7,
                                   GaussianPointInfoDTO p8){
        // p1和p2 选中建筑
        RobotHelper.move(p1.getX(), p1.getY(), 331);
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), 386);
        RobotHelper.mouseLeftPress();
        // p3 朝向建筑
        RobotHelper.move(p3.getX(), p3.getY(), 327);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();

        // p4 停用护盾
        RobotHelper.move(p4.getX(), p4.getY(), 127);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // p5 使用电池
        RobotHelper.move(p5.getX(), p5.getY(), 127);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // p6和p7 上线装备
        RobotHelper.move(p6.getX(), p6.getY(), 127);
        RobotHelper.mouseRightPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(250, 350));
        RobotHelper.move(p7.getX(), p7.getY(), 127);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(850, 950));


        // p6 激活装备
        RobotHelper.move(p6.getX(), p6.getY(), 127);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // p8 进入建筑
        RobotHelper.move(p8.getX(), p8.getY(), 1034);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1530, 1770));
        RobotHelper.mouseLeftPress();

        // p4 启用护盾
        RobotHelper.move(p4.getX(), p4.getY(), 327);
        RobotHelper.mouseLeftPress();
    }


}
