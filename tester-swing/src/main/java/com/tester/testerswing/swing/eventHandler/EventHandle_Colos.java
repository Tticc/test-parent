package com.tester.testerswing.swing.eventHandler;


import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;

/**
 * colos 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36F
 */
public class EventHandle_Colos {

    private static Integer NUM = 3;


    // colos 操作处理
    public static void handle_colos_main(EasyScript_UI_Main script) {
        // 自动投屏事件3
        script.getOnTopReplica_start3().addActionListener((e) -> {
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 colos
        script.getColos_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(false);
            script.getColos_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 colos
        script.getColos_start().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(true);
            script.getColos_status().setText("true");
        });


        // 设置 open 按钮事件。打开账号
        JButton open_colos = script.getOpen_colos();
        open_colos.addActionListener((e) -> {
            try {
                PointHelper.openAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // 设置 run 按钮事件。开工
        JButton open_run_colos = script.getOpen_run_colos();
        open_run_colos.addActionListener((e) -> {
            try {
                PointHelper.eveToWorkAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getColos_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件。收工
        JButton open_end_colos = script.getOpen_end_colos();
        open_end_colos.addActionListener((e) -> {
            try {
                script.getColos_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机
        JButton colos_link = script.getColos_link();
        colos_link.addActionListener((e) -> {
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 放置开工+牵引 按钮事件
        JButton harvest_colos = script.getHarvest_colos();
        harvest_colos.addActionListener((e) -> {
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getColos_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 纯放置牵引 按钮事件
        JButton pure_harvest_colos = script.getPure_harvest_colos();
        pure_harvest_colos.addActionListener((e) -> {
            try {
                PointHelper.doPureHarvestAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void quick_run() {
        try {
            PointHelper.eveEscapeAll(PointHelper.getList(), NUM);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
