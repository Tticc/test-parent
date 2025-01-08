package com.tester.testerswing.swing.eventHandler;


import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * six 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36F
 */
public class EventHandle_Six {

    private static Integer NUM = 6;


    // six 操作处理
    public static void handle_six_main(EasyScript_UI_Main script) {
        // 自动投屏事件3
        script.getOnTopReplica_start6().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 six
        script.getSix_pause().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            script.getAccountInfoList().get(NUM-1).setNeedWarn(false);
            script.getSix_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 six
        script.getSix_start().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            script.getAccountInfoList().get(NUM-1).setNeedWarn(true);
            script.getSix_status().setText("true");
        });


        // 设置 open 按钮事件。打开账号
        JButton open_six = script.getOpen_six();
        open_six.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.openAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // 设置 run 按钮事件。开工
        JButton open_run_six = script.getOpen_run_six();
        open_run_six.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                List<AccountInfo> accountInfoList = script.getAccountInfoList();
                PointHelper.eveToWorkAll(PointHelper.getList(),NUM,accountInfoList);
                // 启动监控
                AccountInfo accountInfo = accountInfoList.get(NUM - 1);
                if(Objects.equals(accountInfo.getSerialNo(), accountInfo.getLeaderSerialNo())){
                    accountInfo.setNeedWarn(true);
                    script.getSix_status().setText("true");
                }else{
                    accountInfo.setNeedWarn(false);
                    script.getSix_status().setText("false");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件。收工
        JButton open_end_six = script.getOpen_end_six();
        open_end_six.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                script.getSix_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机
        JButton six_link = script.getSix_link();
        six_link.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 放置开工+牵引 按钮事件
        JButton harvest_six = script.getHarvest_six();
        harvest_six.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getSix_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 纯放置牵引 按钮事件
        JButton pure_harvest_six = script.getPure_harvest_six();
        pure_harvest_six.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.doPureHarvestAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
