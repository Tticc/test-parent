package com.tester.testerswing.swing.eventHandler;

import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.capture.GaussianStrPointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;
import java.util.List;

/**
 * main 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Main {

    public static void handle_main(EasyScript_UI_Main script) {
        handle_all(script);
        handle_newAccountNeedUpdate(script);
        EventHandle_Silot.handle_silot_main(script);
        EventHandle_Sai.handle_sai_main(script);
        EventHandle_Colos.handle_colos_main(script);
        EventHandle_Four.handle_four_main(script);
    }

    /**
     * 新账号需要调整内容
     */
    private static void handle_newAccountNeedUpdate(EasyScript_UI_Main script) {
        // warn开始
        script.getWarn_start().addActionListener((e) -> {
            script.getWarn_status().setText("true");
            script.getAccountInfoList().get(0).setNeedWarn(true);
            script.getSilot_status().setText("true");
            script.getAccountInfoList().get(1).setNeedWarn(true);
            script.getSai_status().setText("true");
            script.getAccountInfoList().get(2).setNeedWarn(true);
            script.getColos_status().setText("true");
            script.getAccountInfoList().get(3).setNeedWarn(true);
            script.getFour_status().setText("true");
        });

        // warn停止
        script.getWarn_end().addActionListener((e) -> {
            script.getWarn_status().setText("false");
            script.getAccountInfoList().get(0).setNeedWarn(false);
            script.getSilot_status().setText("false");
            script.getAccountInfoList().get(1).setNeedWarn(false);
            script.getSai_status().setText("false");
            script.getAccountInfoList().get(2).setNeedWarn(false);
            script.getColos_status().setText("false");
            script.getAccountInfoList().get(3).setNeedWarn(false);
            script.getFour_status().setText("false");
        });
    }
    private static void handle_all(EasyScript_UI_Main script) {
        // 2 刷新事件
        script.getRefresh().addActionListener((e) -> {
            try {
                String refresh = script.getBoot().refresh();
                script.getRefresh_time().setText(refresh);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        // auto开始
        script.getAuto_start().addActionListener((e) -> {
            script.getAuto_status().setText("true");
            List<AccountInfo> accountInfoList = script.getAccountInfoList();
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setIfAuto(true);
                accountInfo.setLastQuickRunTime(DateUtil.getYesterdayStart());
            }
        });
        // auto停止
        script.getAuto_stop().addActionListener((e) -> {
            script.getAuto_status().setText("false");
            List<AccountInfo> accountInfoList = script.getAccountInfoList();
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setIfAuto(false);
            }
        });

        // 自动投屏事件
        script.getOnTopReplica_start().addActionListener((e) -> {
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),1);
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),null);
        });

        // 全部准备
        JButton all_prepare = script.getAll_prepare();
        all_prepare.addActionListener((e) -> {
            try {
                PointHelper.evePrepareAll(PointHelper.getList(), null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 全部开工
        JButton all_start = script.getAll_start();
        all_start.addActionListener((e) -> {
            try {
                PointHelper.eveToWorkAll(PointHelper.getList(), null);
                // 启动监听
                script.getWarn_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 全部收工
        JButton all_end = script.getAll_end();
        all_end.addActionListener((e) -> {
            try {
                script.getWarn_end().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(), null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 全部开工+牵引
        JButton all_harvest = script.getAll_harvest();
        all_harvest.addActionListener((e) -> {
            try {
                PointHelper.doHarvestAll(PointHelper.getList(), null);
                // 启动监听
                script.getWarn_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 全部纯牵引
        JButton pure_all_harvest = script.getPure_all_harvest();
        pure_all_harvest.addActionListener((e) -> {
            try {
                PointHelper.doPureHarvestAll(PointHelper.getList(), null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


    public static void openOpeNew(GaussianStrPointInfoDTO p1, GaussianStrPointInfoDTO p2) {
        RobotHelper.move(p1.getX(), p1.getY(), GaussianHelper.getGaussianInt(70, 150));
        RobotHelper.mouseLeftPress();
        RobotHelper.move(p2.getX(), p2.getY(), GaussianHelper.getGaussianInt(75, 148));
        RobotHelper.mouseLeftPress();
    }


}
