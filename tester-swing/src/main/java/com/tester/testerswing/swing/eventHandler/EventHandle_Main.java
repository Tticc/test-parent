package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * main 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Main {


    public static PointInfoDTO common_release_drone1 = new PointInfoDTO().setX(1892).setY(770);
    public static PointInfoDTO common_release_drone2 = new PointInfoDTO().setX(1900).setY(780);
    public static GaussianPointInfoDTO common_release_drone = new GaussianPointInfoDTO().setSt(common_release_drone1).setEd(common_release_drone2);

    public static PointInfoDTO common_building_tab1 = new PointInfoDTO().setX(1706).setY(230);
    public static PointInfoDTO common_building_tab2 = new PointInfoDTO().setX(1730).setY(233);
    public static GaussianPointInfoDTO common_building_tab = new GaussianPointInfoDTO().setSt(common_building_tab1).setEd(common_building_tab2);

    public static PointInfoDTO common_fighting_tab1 = new PointInfoDTO().setX(1633).setY(226);
    public static PointInfoDTO common_fighting_tab2 = new PointInfoDTO().setX(1655).setY(236);
    public static GaussianPointInfoDTO common_fighting_tab = new GaussianPointInfoDTO().setSt(common_fighting_tab1).setEd(common_fighting_tab2);

    public static PointInfoDTO common_around1 = new PointInfoDTO().setX(1662).setY(159);
    public static PointInfoDTO common_around2 = new PointInfoDTO().setX(1674).setY(168);
    public static GaussianPointInfoDTO common_around = new GaussianPointInfoDTO().setSt(common_around1).setEd(common_around2);

    public static PointInfoDTO common_speedUp1 = new PointInfoDTO().setX(1107).setY(878);
    public static PointInfoDTO common_speedUp2 = new PointInfoDTO().setX(1122).setY(891);
    public static GaussianPointInfoDTO common_speedUp = new GaussianPointInfoDTO().setSt(common_speedUp1).setEd(common_speedUp2);

    public static PointInfoDTO common_building1 = new PointInfoDTO().setX(1698).setY(573);
    public static PointInfoDTO common_building2 = new PointInfoDTO().setX(1719).setY(581);
    public static GaussianPointInfoDTO common_building = new GaussianPointInfoDTO().setSt(common_building1).setEd(common_building2);


    public static PointInfoDTO common_approach1 = new PointInfoDTO().setX(1701).setY(161);
    public static PointInfoDTO common_approach2 = new PointInfoDTO().setX(1709).setY(171);
    public static GaussianPointInfoDTO common_approach = new GaussianPointInfoDTO().setSt(common_approach1).setEd(common_approach2);


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
                // 打开
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



        // 设置 end 按钮事件 - silot
        JButton open_end_silot = script.getOpen_end_silot();
        open_end_silot.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件 - sai
        JButton open_end_sai = script.getOpen_end_sai();
        open_end_sai.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件 - colos
        JButton open_end_colos = script.getOpen_end_colos();
        open_end_colos.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });




        // 全部结束
        JButton all_end = script.getAll_end();
        all_end.addActionListener((e) -> {
            try {
                // 打开 silot
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

                // 打开 sai
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

                // 打开 colos
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 选中靠近 & 回收无人机
                toApproach();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



        // 全部结束
        JButton all_start = script.getAll_start();
        all_start.addActionListener((e) -> {
            try {
                // 打开 silot
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();

                // 打开 sai
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();

                // 打开 colos
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();

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
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();
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
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDrone();
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

    /**
     *
     */
    public static void toAround(){
        // tab0 切换 生产tab
        RobotHelper.move(common_building_tab.getX(), common_building_tab.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(600, 870));

        // building 选中建筑
        RobotHelper.move(common_building.getX(), common_building.getY(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        // around 环绕建筑
        RobotHelper.move(common_around.getX(), common_around.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(450, 600));

        // tab1 切换 作战tab
        RobotHelper.move(common_fighting_tab.getX(), common_fighting_tab.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));
    }


    /**
     *
     */
    public static void speedUpAndDrone(){

        // speedUp 加速
        RobotHelper.move(common_speedUp.getX(), common_speedUp.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(815, 970));

        // release 释放无人机
        RobotHelper.move(common_release_drone.getX(), common_release_drone.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));
    }


    /**
     *
     */
    public static void toApproach(){

        // 靠近建筑
        RobotHelper.move(common_approach.getX(), common_approach.getY(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        // tab0 切换 生产tab
        RobotHelper.move(common_building_tab.getX(), common_building_tab.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(600, 870));

        // 回收无人机
        RobotHelper.keyPress(KeyEvent.VK_R);
        RobotHelper.delay(GaussianHelper.getGaussianInt(900, 970));
    }


}
