package com.tester.testerswing.swing;

import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.onTopReplica.StartOnTopReplica;
import com.tester.testerswing.robot.RobotHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 初始化公共事件
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class InitCommonEventHelper {

    public static void initCommonEvent(EasyScript_UI_Main script) {

        // 1 直接自动启动监控
        try {
            script.getBoot().start();
            script.getCheck_status().setText("检测中");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // 1.1 同时支持手动启动
        script.getChecking().addActionListener((e) -> {
            try {
                script.getBoot().start();
                script.getCheck_status().setText("检测中");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        // 2 刷新事件
        script.getRefresh().addActionListener((e) -> {
            try {
                String refresh = script.getBoot().refresh();
                script.getRefresh_time().setText(refresh);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 已废弃 2023-9-21 15:16:06
        // 录入人数点阵 事件。 1=silot起点，2=silot终点；3=sai起点，4=sai终点；5=colos起点，6=colos终点
        script.getPoint_input().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyChar： 0=48, 1=49, 2=50
                char keyChar = e.getKeyChar();
                List<AccountInfo> accountInfoList = script.getAccountInfoList();
                String s = setPoint(keyChar, () -> {
                    PointInfoDTO pointInfoDTO = null;
                    if (keyChar == 49) {
                        AccountInfo accountInfo = accountInfoList.get(0);
                        pointInfoDTO = accountInfo.getSt();
                    } else if (keyChar == 50) {
                        AccountInfo accountInfo = accountInfoList.get(0);
                        pointInfoDTO = accountInfo.getEd();
                    } else if (keyChar == 51) {
                        AccountInfo accountInfo = accountInfoList.get(1);
                        pointInfoDTO = accountInfo.getSt();
                    } else if (keyChar == 52) {
                        AccountInfo accountInfo = accountInfoList.get(1);
                        pointInfoDTO = accountInfo.getEd();
                    }else if (keyChar == 53) {
                        AccountInfo accountInfo = accountInfoList.get(2);
                        pointInfoDTO = accountInfo.getEd();
                    }else if (keyChar == 54) {
                        AccountInfo accountInfo = accountInfoList.get(2);
                        pointInfoDTO = accountInfo.getEd();
                    }
                    return pointInfoDTO;
                });
                script.getPoint_print().setText(s);
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // 自动投屏事件
        script.getOnTopReplica_start().addActionListener((e) -> {
            StartOnTopReplica.start();
        });
        // 自动投屏事件1
        script.getOnTopReplica_start1().addActionListener((e) -> {
            StartOnTopReplica.start1();
        });
        // 自动投屏事件2
        script.getOnTopReplica_start2().addActionListener((e) -> {
            StartOnTopReplica.start2();
        });
        // 自动投屏事件3
        script.getOnTopReplica_start3().addActionListener((e) -> {
            StartOnTopReplica.start3();
        });
        // auto开始
        script.getAuto_start().addActionListener((e) -> {
            script.getAuto_status().setText("true");
            script.getAccountInfoList().get(0).setIfAuto(true);
            script.getAccountInfoList().get(0).setLastQuickRunTime(DateUtil.getTodayStart());
            script.getAccountInfoList().get(1).setIfAuto(true);
            script.getAccountInfoList().get(1).setLastQuickRunTime(DateUtil.getTodayStart());
            script.getAccountInfoList().get(2).setIfAuto(true);
            script.getAccountInfoList().get(2).setLastQuickRunTime(DateUtil.getTodayStart());
        });
        // auto停止
        script.getAuto_stop().addActionListener((e) -> {
            script.getAuto_status().setText("false");
            script.getAccountInfoList().get(0).setIfAuto(false);
            script.getAccountInfoList().get(1).setIfAuto(false);
            script.getAccountInfoList().get(2).setIfAuto(false);
        });

        // warn开始
        script.getWarn_start().addActionListener((e) -> {
            script.getWarn_status().setText("true");

            script.getAccountInfoList().get(0).setNeedWarn(true);
            script.getSilot_status().setText("true");
            script.getAccountInfoList().get(1).setNeedWarn(true);
            script.getSai_status().setText("true");
            script.getAccountInfoList().get(2).setNeedWarn(true);
            script.getColos_status().setText("true");
        });
    }


    private static String setPoint(char keyChar, Supplier<PointInfoDTO> supplier) {
        PointInfoDTO pointInfoDTO;
        Point point = RobotHelper.getPoint();
        if (keyChar == 49) {
            String retStr = "silot=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 50) {
            String retStr = "silot=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 51) {
            String retStr = "sai=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 52) {
            String retStr = "sai=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        }else if (keyChar == 53) {
            String retStr = "colos=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        }else if (keyChar == 54) {
            String retStr = "colos=[%d,%d]";
            pointInfoDTO = supplier.get();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        }
        return "";
    }
}
