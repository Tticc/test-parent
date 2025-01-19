package com.tester.testerswing.swing.eventHandler;

import com.google.common.collect.Lists;
import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.capture.GaussianStrPointInfoDTO;
import com.tester.testerswing.gaussian.GaussianHelper;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * main 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Main {

    public static void handle_main(EasyScript_UI_Main script) {
        handle_all(script);
        // 新账号需要调整的。追加：PointHelper的static代码块
        handle_newAccountNeedUpdate(script);
        EventHandle_Silot.handle_silot_main(script);
        EventHandle_Sai.handle_sai_main(script);
        EventHandle_Colos.handle_colos_main(script);
        EventHandle_Four.handle_four_main(script);
        EventHandle_Five.handle_five_main(script);
        EventHandle_Six.handle_six_main(script);
    }

    /**
     * 新账号需要调整内容
     */
    private static void handle_newAccountNeedUpdate(EasyScript_UI_Main script) {

        List<JLabel> labels = new ArrayList<>();
        labels.add(script.getSilot_status());
        labels.add(script.getSai_status());
        labels.add(script.getColos_status());
        labels.add(script.getFour_status());
        labels.add(script.getFive_status());
        labels.add(script.getSix_status());

        // warn开始
        script.getWarn_start().addActionListener((e) -> {
            script.getWarn_status().setText("true");
            for (AccountInfo accountInfo : script.getAccountInfoList()) {
                accountInfo.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode());
                JLabel jLabel = labels.get(accountInfo.getSerialNo() - 1);
                if(accountInfo.checkIfLeader()) {
                    accountInfo.setNeedWarn(true);
                    jLabel.setText("true");
                    accountInfo.setAutoReturnTime(System.currentTimeMillis());
                }else{
                    accountInfo.setNeedWarn(false);
                    jLabel.setText("false");
                }
            }
        });

        // warn停止
        script.getWarn_end().addActionListener((e) -> {
            PointHelper.cleanEsc();
            script.getWarn_status().setText("false");
            for (AccountInfo accountInfo : script.getAccountInfoList()) {
                JLabel jLabel = labels.get(accountInfo.getSerialNo() - 1);
                accountInfo.setNeedWarn(false);
                jLabel.setText("false");
                accountInfo.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode());
            }
        });
    }
    private static void handle_all(EasyScript_UI_Main script) {
//        PointHelper.startListeningForEsc(); // todo
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
            PointHelper.cleanEsc();
            script.getAuto_status().setText("false");
            List<AccountInfo> accountInfoList = script.getAccountInfoList();
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setIfAuto(false);
            }
        });

        // 自动投屏事件
        script.getOnTopReplica_start().addActionListener((e) -> {
            PointHelper.CommonEvePoint commonEvePoint = new PointHelper.CommonEvePoint();
            List<PointHelper.AccountPoint> accountPoints = PointHelper.getList();
            List<PointHelper.AccountPoint> reverse = Lists.reverse(accountPoints);
            for (PointHelper.AccountPoint accountPoint : reverse) {
                EventHandle_Main.openOpeNew(commonEvePoint.getEve_openSelectPoint(), accountPoint.getEve_selectPoint());
                RobotHelper.delay(200);
            }
            PointHelper.onTopReplicaPrepare(accountPoints,null);
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
                List<AccountInfo> accountInfoList = script.getAccountInfoList();
                PointHelper.eveToWorkAll(PointHelper.getList(), null, accountInfoList);
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

        // 整理客户端
        JButton clean_client = script.getClean_client();
        clean_client.addActionListener((e) -> {
            try {
                RobotHelper.delay(GaussianHelper.getGaussianInt(140, 270));
                RobotHelper.keyPress(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
                RobotHelper.delay(GaussianHelper.getGaussianInt(1840, 2070));
                List<AccountInfo> accountInfoList = script.getAccountInfoList();
                for (AccountInfo accountInfo : accountInfoList) {
                    if(Objects.equals(accountInfo.getSerialNo(), accountInfo.getLeaderSerialNo())){
                        PointHelper.openAll(PointHelper.getList(), accountInfo.getSerialNo());
                    }
                }
                RobotHelper.delay(GaussianHelper.getGaussianInt(240, 340));
                script.getWarn_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 打开全部
        JButton open_all = script.getOpen_all();
        open_all.addActionListener((e) -> {
            try {
                PointHelper.openAll(PointHelper.getList(), null);
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
