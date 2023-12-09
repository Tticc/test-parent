package com.tester.testerswing.swing.eventHandler;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static PointInfoDTO all_start1 = new PointInfoDTO().setX(1235).setY(294);
    public static PointInfoDTO all_start2 = new PointInfoDTO().setX(1258).setY(301);
    public static GaussianPointInfoDTO all_start = new GaussianPointInfoDTO().setSt(all_start1).setEd(all_start2);

    public static PointInfoDTO tool_pos1 = new PointInfoDTO().setX(1235).setY(294);
    public static PointInfoDTO tool_pos2 = new PointInfoDTO().setX(1258).setY(301);
    public static GaussianPointInfoDTO tool_pos = new GaussianPointInfoDTO().setSt(tool_pos1).setEd(tool_pos2);

    // 空太空点
    public static PointInfoDTO empty_space_point1 = new PointInfoDTO().setX(908).setY(659);
    public static PointInfoDTO empty_space_point2 = new PointInfoDTO().setX(993).setY(713);
    public static GaussianPointInfoDTO empty_space_point = new GaussianPointInfoDTO().setSt(empty_space_point1).setEd(empty_space_point2);

    // 星图点
    public static PointInfoDTO space_map1 = new PointInfoDTO().setX(13).setY(456);
    public static PointInfoDTO space_map2 = new PointInfoDTO().setX(21).setY(462);
    public static GaussianPointInfoDTO space_map = new GaussianPointInfoDTO().setSt(space_map1).setEd(space_map2);

    // 维修点
    public static PointInfoDTO repair_point1 = new PointInfoDTO().setX(1414).setY(971);
    public static PointInfoDTO repair_point2 = new PointInfoDTO().setX(1429).setY(980);
    public static GaussianPointInfoDTO repair_point = new GaussianPointInfoDTO().setSt(repair_point1).setEd(repair_point2);

    // 中间两项操作。切回作战tab、环绕
    public static List<MyConsumer> MIDDLE_ACTION_LIST = middleActionList();

    // 最后三项操作。加速、环绕、存点
    public static List<MyConsumer> FINAL_ACTION_LIST = finalActionList();

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

        // 设置 open 按钮事件
        JButton open_silot = script.getOpen_silot();
        open_silot.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
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
                speedUpAndDroneAndSavePoint();
                // 启动监控
                script.getSilot_start().doClick();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



        // 设置 end 按钮事件 - silot
        JButton open_end_silot = script.getOpen_end_silot();
        open_end_silot.addActionListener((e) -> {
            try {
                script.getSilot_pause().doClick();
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
                script.getSai_pause().doClick();
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
                script.getColos_pause().doClick();
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
                script.getWarn_end().doClick();
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



        // 全部开工
        JButton all_start = script.getAll_start();
        all_start.addActionListener((e) -> {
            try {
                // 打开 silot
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDroneAndSavePoint();

                // 打开 sai
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDroneAndSavePoint();

                // 打开 colos
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 选中环绕
                toAround();
                // 加速 释放无人机
                speedUpAndDroneAndSavePoint();

                // 启动监听
                script.getWarn_start().doClick();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



        // 全部准备
        JButton all_prepare = script.getAll_prepare();
        all_prepare.addActionListener((e) -> {
            try {
                // 打开 silot
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 准备
                prepare();


                // 打开 sai
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 准备
                prepare();

                // 打开 colos
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 准备
                prepare();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });




        // 设置 找回无人机 - silot
        JButton silot_link = script.getSilot_link();
        silot_link.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
                // 联系 & 回收无人机
                linkDrone();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机 - sai
        JButton sai_link = script.getSai_link();
        sai_link.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
                // 联系 & 回收无人机
                linkDrone();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机 - colos
        JButton colos_link = script.getColos_link();
        colos_link.addActionListener((e) -> {
            try {
                // 打开
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
                // 联系 & 回收无人机
                linkDrone();

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


        // 设置 open 按钮事件
        JButton open_sai = script.getOpen_sai();
        open_sai.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
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
                speedUpAndDroneAndSavePoint();
                // 启动监控
                script.getSai_start().doClick();
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


        // 设置 open 按钮事件
        JButton open_colos = script.getOpen_colos();
        open_colos.addActionListener((e) -> {
            try {
                openOpe(EventHandle_Colos.colos_open_p1, EventHandle_Colos.colos_open_p2);
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
                speedUpAndDroneAndSavePoint();
                // 启动监控
                script.getColos_start().doClick();
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
        RobotHelper.delay(GaussianHelper.getGaussianInt(130, 170));
        RobotHelper.mouseLeftPress();

        // 废弃 2023-9-21 15:28:41
//        // p4 停用护盾
//        RobotHelper.move(p4.getX(), p4.getY(), 127);
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // 废弃 2023-9-21 15:28:41
//        // p5 使用电池
//        RobotHelper.move(p5.getX(), p5.getY(), 127);
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // 废弃 2023-9-21 15:28:41
//        // p6和p7 上线装备
//        RobotHelper.move(p6.getX(), p6.getY(), 127);
//        RobotHelper.mouseRightPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(250, 350));
//        RobotHelper.move(p7.getX(), p7.getY(), 127);
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(850, 950));


        // 废弃 2023-9-21 15:28:41
        // p6 激活装备
//        RobotHelper.move(p6.getX(), p6.getY(), 127);
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
//        RobotHelper.mouseLeftPress();
//        RobotHelper.delay(GaussianHelper.getGaussianInt(50, 200));

        // p8 进入建筑
        RobotHelper.move(p8.getX(), p8.getY(), 1534);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1530, 1770));
        RobotHelper.mouseLeftPress();

        // 废弃 2023-9-21 15:28:41
//        // p4 启用护盾
//        RobotHelper.move(p4.getX(), p4.getY(), 327);
//        RobotHelper.mouseLeftPress();
    }

    /**
     *
     */
    public static void toAround() throws BusinessException {
        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));
        // tab0 切换 生产tab
        RobotHelper.move(common_building_tab.getX(), common_building_tab.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(600, 870));

        // building 选中建筑
        RobotHelper.move(common_building.getX(), common_building.getY(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));

        Collections.shuffle(MIDDLE_ACTION_LIST);
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            myConsumer.accept(null);
        }
        /*// tab1 切换 作战tab
        RobotHelper.move(common_fighting_tab.getX(), common_fighting_tab.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));

        // around 环绕建筑
        RobotHelper.move(common_around.getX(), common_around.getY(), 100);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(450, 600));*/
    }

    private static List<MyConsumer> middleActionList(){
        List<MyConsumer> list = new ArrayList<>();
        list.add((e) -> {
            // tab1 切换 作战tab
            RobotHelper.move(common_fighting_tab.getX(), common_fighting_tab.getY(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(1060, 1100));
        });
        list.add((e) -> {
            // around 环绕建筑
            RobotHelper.move(common_around.getX(), common_around.getY(), 100);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(30, 70));
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(450, 600));
        });
        return list;
    }

    private static List<MyConsumer> finalActionList(){
        List<MyConsumer> list = new ArrayList<>();
        list.add((e) -> {
            // speedUp 加速
            RobotHelper.move(common_speedUp.getX(), common_speedUp.getY(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(815, 970));
        });
        list.add((e) -> {
            // release 释放无人机
            RobotHelper.move(common_release_drone.getX(), common_release_drone.getY(), 94);
            RobotHelper.mouseLeftPress();
            RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));
        });
        list.add((e) -> {
            // 保存点
            savePoint();
        });
        return list;
    }

    /**
     *
     */
    public static void speedUpAndDroneAndSavePoint() throws BusinessException {
        List<MyConsumer> executeList = FINAL_ACTION_LIST;
        boolean shuffle = Math.random()*10 < 1;
        if(shuffle){
            List<MyConsumer> tempList = new ArrayList<>(FINAL_ACTION_LIST);
            Collections.shuffle(tempList);
            executeList = tempList;
        }
        for (MyConsumer myConsumer : executeList) {
            myConsumer.accept(null);
        }
        RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));
        /*// speedUp 加速
        RobotHelper.move(common_speedUp.getX(), common_speedUp.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(815, 970));

        // release 释放无人机
        RobotHelper.move(common_release_drone.getX(), common_release_drone.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(615, 770));

        // 保存点
        savePoint();*/
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


    /**
     * 回收无人机
     */
    public static void linkDrone() throws BusinessException {

        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));

        // tab0 切换 生产tab
        RobotHelper.move(common_building_tab.getX(), common_building_tab.getY(), 94);
        RobotHelper.mouseLeftPress();

        RobotHelper.delay(GaussianHelper.getGaussianInt(400, 470));
        // 联系无人机
        RobotHelper.keyPress(KeyEvent.VK_N);
        RobotHelper.delay(GaussianHelper.getGaussianInt(2500, 2770));

        // 回收无人机
        RobotHelper.keyPress(KeyEvent.VK_R);
        RobotHelper.delay(GaussianHelper.getGaussianInt(200, 270));


        // building 选中建筑
        RobotHelper.move(common_building.getX(), common_building.getY(), 88);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(330, 370));


        // 环绕+切回作战tab
        for (MyConsumer myConsumer : MIDDLE_ACTION_LIST) {
            myConsumer.accept(null);
        }
    }

    /**
     * 存点
     */
    public static void savePoint(){
        RobotHelper.keyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_B);
        RobotHelper.delay(GaussianHelper.getGaussianInt(240, 370));
        RobotHelper.keyPress(KeyEvent.VK_ENTER);
    }


    public static void prepare(){
        // 单击太空
        RobotHelper.move(empty_space_point.getX(), empty_space_point.getY(), 94);
        RobotHelper.mouseLeftPress();

        // 滑动鼠标，拉远舰船
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));
        for (int i = 0; i < 5; i++) {
            RobotHelper.mouseWheel(-4);
        }

        // ctrl+shift+f9
        RobotHelper.delay(GaussianHelper.getGaussianInt(524, 670));
        RobotHelper.keyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_F9);

        // 打开星图
        RobotHelper.move(space_map.getX(), space_map.getY(), 194);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(3024, 3370));

        // 单击太空
        RobotHelper.move(empty_space_point.getX(), empty_space_point.getY(), 94);
        RobotHelper.mouseLeftPress();
        // 滑动鼠标，拉近星图
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));
        RobotHelper.mouseWheel(3);
        RobotHelper.mouseWheel(2);
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

        // 启动维修
        RobotHelper.move(repair_point.getX(), repair_point.getY(), 94);
        RobotHelper.mouseLeftPress();
        RobotHelper.delay(GaussianHelper.getGaussianInt(424, 470));

    }


}
