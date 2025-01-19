package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;
import com.tester.testerswing.swing.Sai_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;

/**
 * sai 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Sai {

    private static Integer NUM = 2;

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO sai_open_p1_st = new PointInfoDTO().setX(1212).setY(1048);
    public static GaussianPointInfoDTO sai_open_p1 = new GaussianPointInfoDTO().setSt(sai_open_p1_st).setEd(sai_open_p1_st);
    public static PointInfoDTO sai_open_p2_st = new PointInfoDTO().setX(1233).setY(961);
    public static GaussianPointInfoDTO sai_open_p2 = new GaussianPointInfoDTO().setSt(sai_open_p2_st).setEd(sai_open_p2_st);


    public static PointInfoDTO sai_return_p1_st = new PointInfoDTO().setX(1677).setY(733);
    public static PointInfoDTO sai_return_p1_ed = new PointInfoDTO().setX(1701).setY(745);
    public static GaussianPointInfoDTO sai_return_p1 = new GaussianPointInfoDTO().setSt(sai_return_p1_st).setEd(sai_return_p1_ed);

    public static PointInfoDTO sai_return_p2_st = new PointInfoDTO().setX(1629).setY(811);
    public static PointInfoDTO sai_return_p2_ed = new PointInfoDTO().setX(1629).setY(811);
    public static GaussianPointInfoDTO sai_return_p2 = new GaussianPointInfoDTO().setSt(sai_return_p2_st).setEd(sai_return_p2_ed);

    public static PointInfoDTO sai_return_p3_st = new PointInfoDTO().setX(1708).setY(881);
    public static PointInfoDTO sai_return_p3_ed = new PointInfoDTO().setX(1734).setY(888);
    public static GaussianPointInfoDTO sai_return_p3 = new GaussianPointInfoDTO().setSt(sai_return_p3_st).setEd(sai_return_p3_ed);

    public static PointInfoDTO sai_align_p1_st = new PointInfoDTO().setX(1846).setY(230);
    public static PointInfoDTO sai_align_p1_ed = new PointInfoDTO().setX(1865).setY(239);
    public static GaussianPointInfoDTO sai_align_p1 = new GaussianPointInfoDTO().setSt(sai_align_p1_st).setEd(sai_align_p1_ed);

    public static PointInfoDTO sai_align_p2_st = new PointInfoDTO().setX(1625).setY(288);
    public static PointInfoDTO sai_align_p2_ed = new PointInfoDTO().setX(1644).setY(299);
    public static GaussianPointInfoDTO sai_align_p2 = new GaussianPointInfoDTO().setSt(sai_align_p2_st).setEd(sai_align_p2_ed);

    public static PointInfoDTO sai_align_p3_st = new PointInfoDTO().setX(1590).setY(154);
    public static PointInfoDTO sai_align_p3_ed = new PointInfoDTO().setX(1601).setY(163);
    public static GaussianPointInfoDTO sai_align_p3 = new GaussianPointInfoDTO().setSt(sai_align_p3_st).setEd(sai_align_p3_ed);

    public static PointInfoDTO sai_align_p3_quick_st = new PointInfoDTO().setX(1664).setY(151);
    public static PointInfoDTO sai_align_p3_quick_ed = new PointInfoDTO().setX(1678).setY(165);
    public static GaussianPointInfoDTO sai_align_p3_quick = new GaussianPointInfoDTO().setSt(sai_align_p3_quick_st).setEd(sai_align_p3_quick_ed);


    public static PointInfoDTO sai_main_view_p1_st = new PointInfoDTO().setX(1630).setY(229);
    public static PointInfoDTO sai_main_view_p1_ed = new PointInfoDTO().setX(1646).setY(239);
    public static GaussianPointInfoDTO sai_main_view_p1 = new GaussianPointInfoDTO().setSt(sai_main_view_p1_st).setEd(sai_main_view_p1_ed);


    public static void handle_sai(Sai_Input script) {
        // 设置输入事件
        JTextField sai_open_p1_input = script.getSai_open_p1_input();
        setTextField(sai_open_p1_input, sai_open_p1_st);

        JTextField sai_open_p2_input = script.getSai_open_p2_input();
        setTextField(sai_open_p2_input, sai_open_p2_st);


        // 返回6个点
        JTextField sai_return_p1_input = script.getSai_return_p1_input();
        setTextField(sai_return_p1_input, sai_return_p1_st);
        JTextField sai_return_p12_input = script.getSai_return_p12_input();
        setTextField(sai_return_p12_input, sai_return_p1_ed);

        JTextField sai_return_p2_input = script.getSai_return_p2_input();
        setTextField(sai_return_p2_input, sai_return_p2_st);
        JTextField sai_return_p22_input = script.getSai_return_p22_input();
        setTextField(sai_return_p22_input, sai_return_p2_ed);

        JTextField sai_return_p3_input = script.getSai_return_p3_input();
        setTextField(sai_return_p3_input, sai_return_p3_st);
        JTextField sai_return_p32_input = script.getSai_return_p32_input();
        setTextField(sai_return_p32_input, sai_return_p3_ed);


        // 跑路6个点
        JTextField sai_align_p1_input = script.getSai_align_p1_input();
        setTextField(sai_align_p1_input, sai_align_p1_st);
        JTextField sai_align_p12_input = script.getSai_align_p12_input();
        setTextField(sai_align_p12_input, sai_align_p1_ed);

        JTextField sai_align_p2_input = script.getSai_align_p2_input();
        setTextField(sai_align_p2_input, sai_align_p2_st);
        JTextField sai_align_p22_input = script.getSai_align_p22_input();
        setTextField(sai_align_p22_input, sai_align_p2_ed);

        JTextField sai_align_p3_input = script.getSai_align_p3_input();
        setTextField(sai_align_p3_input, sai_align_p3_st);
        JTextField sai_align_p32_input = script.getSai_align_p32_input();
        setTextField(sai_align_p32_input, sai_align_p3_ed);

        // 主览2个点
        JTextField sai_main_view_input = script.getSai_main_view_input();
        setTextField(sai_main_view_input, sai_main_view_p1_st);
        JTextField sai_main_view2_input = script.getSai_main_view2_input();
        setTextField(sai_main_view2_input, sai_main_view_p1_ed);

    }

    // sai 操作处理
    public static void handle_sai_main(EasyScript_UI_Main script) {
        // 自动投屏事件2
        script.getOnTopReplica_start2().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 sai
        script.getSai_pause().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            AccountInfo accountInfo = script.getAccountInfoList().get(NUM - 1);
            accountInfo.setNeedWarn(false);
            if(accountInfo.checkIfLeader()) {
                accountInfo.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode());
                accountInfo.getFollows().values().forEach(acc -> acc.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode()));
            }
            script.getSai_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 sai
        script.getSai_start().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            AccountInfo accountInfo = script.getAccountInfoList().get(NUM - 1);
            accountInfo.setNeedWarn(true);
            if(accountInfo.checkIfLeader()) {
                accountInfo.setAutoReturnTime(System.currentTimeMillis());
            }
            script.getSai_status().setText("true");
        });


        // 设置 open 按钮事件。打开账号
        JButton open_sai = script.getOpen_sai();
        open_sai.addActionListener((e) -> {
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
        JButton open_run_sai = script.getOpen_run_sai();
        open_run_sai.addActionListener((e) -> {
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
                    script.getSai_status().setText("true");
                }else{
                    accountInfo.setNeedWarn(false);
                    script.getSai_status().setText("false");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 end 按钮事件。收工
        JButton open_end_sai = script.getOpen_end_sai();
        open_end_sai.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                script.getSai_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        // 设置 找回无人机
        JButton sai_link = script.getSai_link();
        sai_link.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置sai 放置开工+牵引 按钮事件
        JButton harvest_sai = script.getHarvest_sai();
        harvest_sai.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getSai_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 纯放置牵引 按钮事件
        JButton pure_harvest_sai = script.getPure_harvest_sai();
        pure_harvest_sai.addActionListener((e) -> {
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


    private static void setTextField(JTextField field, PointInfoDTO sPoint) {
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyChar： 0=48, 1=49, 2=50
                // keyChar： a=97, b=98
                char keyChar = e.getKeyChar();
                if (keyChar == 97) {
                    Point point = RobotHelper.getPoint();
                    sPoint.setX((int) point.getX());
                    sPoint.setY((int) point.getY());
                    field.setText(String.format(pointStr, sPoint.getX(), sPoint.getY()));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }


}
