package com.tester.testerswing.swing.eventHandler;


import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * five 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36F
 */
public class EventHandle_Five {

    private static Integer NUM = 5;


    // five 操作处理
    public static void handle_five_main(EasyScript_UI_Main script) {
        // 自动投屏事件3
        script.getOnTopReplica_start5().addActionListener((e) -> {
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 five
        script.getFive_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(false);
            script.getFive_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 five
        script.getFive_start().addActionListener((e) -> {
            script.getAccountInfoList().get(NUM-1).setNeedWarn(true);
            script.getFive_status().setText("true");
        });


        // 设置 open 按钮事件。打开账号
        JButton open_five = script.getOpen_five();
        open_five.addActionListener((e) -> {
            try {
                PointHelper.openAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // 设置 run 按钮事件。开工
        JButton open_run_five = script.getOpen_run_five();
        open_run_five.addActionListener((e) -> {
            try {
                List<AccountInfo> accountInfoList = script.getAccountInfoList();
                PointHelper.eveToWorkAll(PointHelper.getList(),NUM, accountInfoList);
                // 启动监控
                AccountInfo accountInfo = accountInfoList.get(NUM - 1);
                if(Objects.equals(accountInfo.getSerialNo(), accountInfo.getLeaderSerialNo())){
                    accountInfo.setNeedWarn(true);
                    script.getFive_status().setText("true");
                }else{
                    accountInfo.setNeedWarn(false);
                    script.getFive_status().setText("false");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件。收工
        JButton open_end_five = script.getOpen_end_five();
        open_end_five.addActionListener((e) -> {
            try {
                script.getFive_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机
        JButton five_link = script.getFive_link();
        five_link.addActionListener((e) -> {
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 放置开工+牵引 按钮事件
        JButton harvest_five = script.getHarvest_five();
        harvest_five.addActionListener((e) -> {
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getFive_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 纯放置牵引 按钮事件
        JButton pure_harvest_five = script.getPure_harvest_five();
        pure_harvest_five.addActionListener((e) -> {
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
