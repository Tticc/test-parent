package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.EasyScript_UI_Main;
import com.tester.testerswing.swing.Silot_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * silot 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Silot {

    private static Integer NUM = 1;

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO silot_open_p1_st = new PointInfoDTO().setX(1212).setY(1051);
    public static GaussianPointInfoDTO silot_open_p1 = new GaussianPointInfoDTO().setSt(silot_open_p1_st).setEd(silot_open_p1_st);
    public static PointInfoDTO silot_open_p2_st = new PointInfoDTO().setX(937).setY(948);
    public static GaussianPointInfoDTO silot_open_p2 = new GaussianPointInfoDTO().setSt(silot_open_p2_st).setEd(silot_open_p2_st);

    public static PointInfoDTO silot_return_p1_st = new PointInfoDTO().setX(1675).setY(732);
    public static PointInfoDTO silot_return_p1_ed = new PointInfoDTO().setX(1706).setY(743);
    public static GaussianPointInfoDTO silot_return_p1 = new GaussianPointInfoDTO().setSt(silot_return_p1_st).setEd(silot_return_p1_ed);

    public static PointInfoDTO silot_return_p2_st = new PointInfoDTO().setX(1652).setY(811);
    public static PointInfoDTO silot_return_p2_ed = new PointInfoDTO().setX(1652).setY(811);
    public static GaussianPointInfoDTO silot_return_p2 = new GaussianPointInfoDTO().setSt(silot_return_p2_st).setEd(silot_return_p2_ed);

    public static PointInfoDTO silot_return_p3_st = new PointInfoDTO().setX(1723).setY(880);
    public static PointInfoDTO silot_return_p3_ed = new PointInfoDTO().setX(1747).setY(887);
    public static GaussianPointInfoDTO silot_return_p3 = new GaussianPointInfoDTO().setSt(silot_return_p3_st).setEd(silot_return_p3_ed);

    public static PointInfoDTO silot_align_p1_st = new PointInfoDTO().setX(1850).setY(231);
    public static PointInfoDTO silot_align_p1_ed = new PointInfoDTO().setX(1865).setY(238);
    public static GaussianPointInfoDTO silot_align_p1 = new GaussianPointInfoDTO().setSt(silot_align_p1_st).setEd(silot_align_p1_ed);

    public static PointInfoDTO silot_align_p2_st = new PointInfoDTO().setX(1622).setY(290);
    public static PointInfoDTO silot_align_p2_ed = new PointInfoDTO().setX(1650).setY(298);
    public static GaussianPointInfoDTO silot_align_p2 = new GaussianPointInfoDTO().setSt(silot_align_p2_st).setEd(silot_align_p2_ed);

    public static PointInfoDTO silot_align_p3_st = new PointInfoDTO().setX(1588).setY(153);
    public static PointInfoDTO silot_align_p3_ed = new PointInfoDTO().setX(1602).setY(163);
    public static GaussianPointInfoDTO silot_align_p3 = new GaussianPointInfoDTO().setSt(silot_align_p3_st).setEd(silot_align_p3_ed);


    public static PointInfoDTO silot_align_p3_dock_st = new PointInfoDTO().setX(1663).setY(153);
    public static PointInfoDTO silot_align_p3_dock_ed = new PointInfoDTO().setX(1676).setY(162);
    public static GaussianPointInfoDTO silot_align_p3_dock = new GaussianPointInfoDTO().setSt(silot_align_p3_dock_st).setEd(silot_align_p3_dock_ed);

    public static PointInfoDTO silot_align_p4_stop_st = new PointInfoDTO().setX(1060).setY(880);
    public static PointInfoDTO silot_align_p4_stop_ed = new PointInfoDTO().setX(1072).setY(890);
    public static GaussianPointInfoDTO silot_align_p4_stop = new GaussianPointInfoDTO().setSt(silot_align_p4_stop_st).setEd(silot_align_p4_stop_ed);

    public static PointInfoDTO silot_align_p5_use_st = new PointInfoDTO().setX(1108).setY(876);
    public static PointInfoDTO silot_align_p5_use_ed = new PointInfoDTO().setX(1126).setY(895);
    public static GaussianPointInfoDTO silot_align_p5_use = new GaussianPointInfoDTO().setSt(silot_align_p5_use_st).setEd(silot_align_p5_use_ed);

    public static PointInfoDTO silot_align_p6_up_st = new PointInfoDTO().setX(1169).setY(884);
    public static PointInfoDTO silot_align_p6_up_ed = new PointInfoDTO().setX(1169).setY(884);
    public static GaussianPointInfoDTO silot_align_p6_up = new GaussianPointInfoDTO().setSt(silot_align_p6_up_st).setEd(silot_align_p6_up_ed);

    public static PointInfoDTO silot_align_p7_up_st = new PointInfoDTO().setX(1213).setY(933);
    public static PointInfoDTO silot_align_p7_up_ed = new PointInfoDTO().setX(1246).setY(941);
    public static GaussianPointInfoDTO silot_align_p7_up = new GaussianPointInfoDTO().setSt(silot_align_p7_up_st).setEd(silot_align_p7_up_ed);


    public static PointInfoDTO silot_main_view_p1_st = new PointInfoDTO().setX(1633).setY(229);
    public static PointInfoDTO silot_main_view_p1_ed = new PointInfoDTO().setX(1651).setY(237);
    public static GaussianPointInfoDTO silot_main_view_p1 = new GaussianPointInfoDTO().setSt(silot_main_view_p1_st).setEd(silot_main_view_p1_ed);


    public static void handle_silot(Silot_Input script, List<AccountInfo> accountInfoList) {
        // 取点用
        getPointInfo(script);
        // 初始化leader follow
        // !!!!!初始化数据，调用onClick
        leaderShift(script, accountInfoList);
        // 初始化环绕残骸
        initAroundTrash(script, accountInfoList);
        // 初始化账号变更
        initAccountNum(script);
        // 设置警卫状态
        initGuardStatus(script, accountInfoList);
        // 设置旗舰监控
        initWatchCapitalStatus(script, accountInfoList);
    }

    // button 忽略旗舰
    // button 监控旗舰
    private static void initWatchCapitalStatus(Silot_Input script, List<AccountInfo> accountInfoList){
        JButton watch_capital = script.getWatch_capital();
        JButton unwatch_capital = script.getUnwatch_capital();
        JLabel isWatchingCapital = script.getIsWatchingCapital();
        watch_capital.addActionListener((e) -> {
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setWatchCapitalEnable(true);
            }
            isWatchingCapital.setText("true");
        });
        unwatch_capital.addActionListener((e) -> {
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setWatchCapitalEnable(false);
            }
            isWatchingCapital.setText("false");
        });
        // 默认为 监控旗舰
        watch_capital.doClick();
    }

    private static void initGuardStatus(Silot_Input script, List<AccountInfo> accountInfoList){
        JButton use_guard = script.getUse_guard();
        JButton ban_guard = script.getBan_guard();
        JLabel hasGuard = script.getHasGuard();
        use_guard.addActionListener((e) -> {
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setHasGuard(true);
            }
            hasGuard.setText("true");
        });
        ban_guard.addActionListener((e) -> {
            for (AccountInfo accountInfo : accountInfoList) {
                accountInfo.setHasGuard(false);
            }
            hasGuard.setText("false");
        });
        ban_guard.doClick();
    }
    private static void initAccountNum(Silot_Input script){
        script.getAccountNum1().setText(Boot.getAccountNum()+"");
        script.getAccountNum2().setText(Boot.getAccountNum()+"");
        JButton reduce_use = script.getReduce_use();
        JButton add_use = script.getAdd_use();
        reduce_use.addActionListener((e) -> {
            Integer newNum = Boot.getAccountNum()-1;
            if(newNum < 3){
                return;
            }
            Boot.setAccountNum(newNum);
            script.getAccountNum1().setText(Boot.getAccountNum()+"");
            script.getAccountNum2().setText(Boot.getAccountNum()+"");
        });
        add_use.addActionListener((e) -> {
            Boot.setAccountNum(Boot.getAccountNum()+1);
            script.getAccountNum1().setText(Boot.getAccountNum()+"");
            script.getAccountNum2().setText(Boot.getAccountNum()+"");
        });

        // button reduce_use
        // button add_use
        // 初始化账号数量为4
//        RobotHelper.delay(200);
//        reduce_use.doClick();
        RobotHelper.delay(200);
        reduce_use.doClick();
        RobotHelper.delay(200);
        reduce_use.doClick();
        RobotHelper.delay(200);


    }
    private static void initAroundTrash(Silot_Input script, List<AccountInfo> accountInfoList){
        List<JLabel> labels = new ArrayList<>();
        labels.add(script.getAround_trash_status1());
        labels.add(script.getAround_trash_status2());
        labels.add(script.getAround_trash_status3());
        labels.add(script.getAround_trash_status4());
        labels.add(script.getAround_trash_status5());
        labels.add(script.getAround_trash_status6());

        List<JButton> buttons = new ArrayList<>();
        buttons.add(script.getAround_trash1());
        buttons.add(script.getAround_trash2());
        buttons.add(script.getAround_trash3());
        buttons.add(script.getAround_trash4());
        buttons.add(script.getAround_trash5());
        buttons.add(script.getAround_trash6());

        for (JLabel label : labels) {
            label.setText("false");
        }
        JButton init_trash = script.getInit_trash();
        init_trash.addActionListener((e) -> {
            try {
                for (AccountInfo accountInfo : accountInfoList) {
                    accountInfo.setArroundTrash(false);
                }
                for (JLabel label : labels) {
                    label.setText("false");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        for (int i = 0; i < buttons.size(); i++) {
            int index = i;
            JButton jButton = buttons.get(index);
            jButton.addActionListener((e) -> {
                Integer currNum = index+1;
                try {
                    labels.get(index).setText("true");
                    for (AccountInfo accountInfo : accountInfoList) {
                        if(Objects.equals(accountInfo.getSerialNo(), currNum)){
                            accountInfo.setArroundTrash(true);
                            return;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        // button 1残骸
        script.getAround_trash1().doClick();
        RobotHelper.delay(200);
        script.getAround_trash2().doClick();
        RobotHelper.delay(200);
        script.getAround_trash3().doClick();
    }
    private static void leaderShift(Silot_Input script, List<AccountInfo> accountInfoList){
        List<JLabel> labels = new ArrayList<>();
        labels.add(script.getLeader_1());
        labels.add(script.getLeader_2());
        labels.add(script.getLeader_3());
        labels.add(script.getLeader_4());
        labels.add(script.getLeader_5());
        labels.add(script.getLeader_6());
        for (JLabel label : labels) {
            label.setText("leader");
        }

        JButton init_shift = script.getInit_shift();
        init_shift.addActionListener((e) -> {
            try {
                for (AccountInfo accountInfo : accountInfoList) {
                    accountInfo.setLeaderSerialNo(accountInfo.getSerialNo());
                    accountInfo.setFollows(new HashMap<>());
                }
                for (JLabel label : labels) {
                    label.setText("leader");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_1 = script.getLead_shift_1();
        lead_shift_1.addActionListener((e) -> {
            Integer currNum = 1;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_2 = script.getLead_shift_2();
        lead_shift_2.addActionListener((e) -> {
            Integer currNum = 2;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_3 = script.getLead_shift_3();
        lead_shift_3.addActionListener((e) -> {
            Integer currNum = 3;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_4 = script.getLead_shift_4();
        lead_shift_4.addActionListener((e) -> {
            Integer currNum = 4;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_5 = script.getLead_shift_5();
        lead_shift_5.addActionListener((e) -> {
            Integer currNum = 5;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        JButton lead_shift_6 = script.getLead_shift_6();
        lead_shift_6.addActionListener((e) -> {
            Integer currNum = 6;
            try {
                doShift(accountInfoList, currNum, labels);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
//        lead_shift_2.doClick();
//        RobotHelper.delay(200);
//        lead_shift_6.doClick();
//        RobotHelper.delay(200);
//        lead_shift_4.doClick();
//        RobotHelper.delay(200);
//        lead_shift_4.doClick();
//        RobotHelper.delay(200);

    }

    private static void doShift(List<AccountInfo> accountInfoList, Integer currNum, List<JLabel> labels){
        Map<Integer, AccountInfo> accountInfoMap= accountInfoList
                .stream()
                .collect(Collectors.toMap(item -> item.getSerialNo(), Function.identity(), (oldValue, newValue) -> oldValue));
        AccountInfo currentAccount = null;
        AccountInfo currLeaderAccount = null;
        for (AccountInfo accountInfo : accountInfoList) {
            if(Objects.equals(accountInfo.getSerialNo(), currNum)){
                currentAccount = accountInfo;
                break;
            }
        }
        for (AccountInfo accountInfo : accountInfoList) {
            if(Objects.equals(accountInfo.getSerialNo(), currentAccount.getLeaderSerialNo())){
                currLeaderAccount = accountInfo;
                break;
            }
        }
        AccountInfo newLeaderAccount = null;
        for (AccountInfo accountInfo : accountInfoList) {
            if(Objects.equals(accountInfo.getSerialNo(), currentAccount.getSerialNo())){
                continue;
            }
            if(null == newLeaderAccount
                    && Objects.equals(accountInfo.getSerialNo(), accountInfo.getLeaderSerialNo())
                    && (Objects.equals(currentAccount.getSerialNo(), currLeaderAccount.getSerialNo()) ? 0 : currLeaderAccount.getSerialNo()) < accountInfo.getSerialNo()){
                newLeaderAccount = accountInfo;
            }
        }
        if(null ==  currentAccount || null == newLeaderAccount){
            return;
        }
        doShiftLeader(accountInfoMap, currentAccount, newLeaderAccount, labels, currNum);
        // 刷新currentAccount的follow到newLeaderAccount
        for (AccountInfo accountInfo : currentAccount.getFollows().values()) {
            if(Objects.equals(accountInfo.getLeaderSerialNo(),currentAccount.getSerialNo())){
                doShiftLeader(accountInfoMap, accountInfo, newLeaderAccount, labels, accountInfo.getSerialNo());
            }
        }
    }

    private static void doShiftLeader(Map<Integer, AccountInfo> accountInfoMap,
                               AccountInfo currentAccount,
                               AccountInfo newLeaderAccount,
                               List<JLabel> labels,
                               Integer currNum){
        AccountInfo oldLeader = accountInfoMap.get(currentAccount.getLeaderSerialNo());
        // 从旧leader的follow中移除当前
        oldLeader.getFollows().remove(currentAccount.getSerialNo());
        currentAccount.setLeaderSerialNo(newLeaderAccount.getSerialNo());
        // 将当前添加到新leader的follow中
        newLeaderAccount.getFollows().put(currentAccount.getSerialNo(), currentAccount);
        labels.get(currNum-1).setText("follow  "+newLeaderAccount.getSerialNo());
    }


    // silot 操作处理
    public static void handle_silot_main(EasyScript_UI_Main script) {
        // 自动投屏事件1
        script.getOnTopReplica_start1().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            PointHelper.onTopReplicaPrepare(PointHelper.getList(),NUM);
        });
        // 暂停 silot
        script.getSilot_pause().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            AccountInfo accountInfo = script.getAccountInfoList().get(NUM - 1);
            accountInfo.setNeedWarn(false);
            if(accountInfo.checkIfLeader()) {
                accountInfo.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode());
                accountInfo.getFollows().values().forEach(acc -> acc.setGuardStatus(AccountInfo.GuardStatusEnum.STAND_BY.getCode()));
            }
            script.getSilot_status().setText("false");
            script.getWarn_status().setText("false");
        });
        // 继续 silot
        script.getSilot_start().addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            AccountInfo accountInfo = script.getAccountInfoList().get(NUM - 1);
            accountInfo.setNeedWarn(true);
            if(accountInfo.checkIfLeader()) {
                accountInfo.setAutoReturnTime(System.currentTimeMillis());
            }
            script.getSilot_status().setText("true");
        });

        // 设置 open 按钮事件。打开账号
        JButton open_silot = script.getOpen_silot();
        open_silot.addActionListener((e) -> {
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
        JButton open_run_silot = script.getOpen_run_silot();
        open_run_silot.addActionListener((e) -> {
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
                    script.getSilot_status().setText("true");
                }else{
                    accountInfo.setNeedWarn(false);
                    script.getSilot_status().setText("false");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // 设置 end 按钮事件。收工
        JButton open_end_silot = script.getOpen_end_silot();
        open_end_silot.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                script.getSilot_pause().doClick();
                PointHelper.eveEndWorkAll(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 找回无人机
        JButton silot_link = script.getSilot_link();
        silot_link.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.linkDrone(PointHelper.getList(),NUM);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置silot 放置开工+牵引 按钮事件
        JButton harvest_silot = script.getHarvest_silot();
        harvest_silot.addActionListener((e) -> {
            if(Boot.checkIfReturn(NUM)){
                return;
            }
            try {
                PointHelper.doHarvestAll(PointHelper.getList(),NUM);
                // 启动监控
                script.getSilot_start().doClick();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 设置 纯放置牵引 按钮事件
        JButton pure_harvest_silot = script.getPure_harvest_silot();
        pure_harvest_silot.addActionListener((e) -> {
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




    private static void getPointInfo(Silot_Input script) {
        // 设置输入事件
        JTextField silot_open_p1_input = script.getSilot_open_p1_input();
        setTextField(silot_open_p1_input, silot_open_p1_st);

        JTextField silot_open_p2_input = script.getSilot_open_p2_input();
        setTextField(silot_open_p2_input, silot_open_p2_st);

        // 返回6个点
        JTextField silot_return_p1_input = script.getSilot_return_p1_input();
        setTextField(silot_return_p1_input, silot_return_p1_st);
        JTextField silot_return_p12_input = script.getSilot_return_p12_input();
        setTextField(silot_return_p12_input, silot_return_p1_ed);
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
