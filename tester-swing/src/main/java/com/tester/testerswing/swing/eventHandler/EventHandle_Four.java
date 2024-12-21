package com.tester.testerswing.swing.eventHandler;


import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;

/**
 * four 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36F
 */
public class EventHandle_Four {

    private static Integer NUM = 4;


    // four 操作处理
    public static void handle_four_main(EasyScript_UI_Main script) {
        // 自动投屏事件3
        script.getOnTopReplica_start4().addActionListener((e) -> {
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 four
        script.getFour_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(false);
            script.getFour_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 four
        script.getFour_start().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(true);
            script.getFour_status().setText("true");
        });


        // 设置 open 按钮事件。打开账号
        JButton open_four = script.getOpen_four();
        open_four.addActionListener((e) -> {
            try {
                PointHelper.openAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // 设置 run 按钮事件。开工
        JButton open_run_four = script.getOpen_run_four();
        open_run_four.addActionListener((e) -> {
            try {
                PointHelper.eveToWorkAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getFour_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件。收工
        JButton open_end_four = script.getOpen_end_four();
        open_end_four.addActionListener((e) -> {
            try {
                script.getFour_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机
        JButton four_link = script.getFour_link();
        four_link.addActionListener((e) -> {
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 放置牵引 按钮事件
        JButton harvest_four = script.getHarvest_four();
        harvest_four.addActionListener((e) -> {
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getFour_start().doClick();
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
