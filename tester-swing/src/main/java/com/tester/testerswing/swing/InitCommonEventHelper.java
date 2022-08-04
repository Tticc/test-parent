package com.tester.testerswing.swing;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * 初始化公共事件
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class InitCommonEventHelper {

    public static void initCommonEvent(EasyScript_UI script) {
        // 触发检测监控
        script.getChecking().addActionListener((e) -> {
            try {
                script.getBoot().start();
                script.getCheck_status().setText("检测中");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        // 刷新事件
        script.getRefresh().addActionListener((e) -> {
            try {
                String refresh = script.getBoot().refresh();
                script.getRefresh_time().setText(refresh);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // 录入点阵 事件。 1=silot起点，2=silot终点；3=sai起点，4=sai终点
        script.getPoint_input().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // keyChar： 0=48, 1=49, 2=50
                char keyChar = e.getKeyChar();
                String s = setPoint(keyChar, script.getAccountInfoList());
                script.getPoint_print().setText(s);
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // 暂停 silot
        script.getSilot_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(false);
            script.getSilot_status().setText("false");
        });
        // 继续 silot
        script.getSilot_start().addActionListener((e) -> {
            script.getAccountInfoList().get(0).setNeedWarn(true);
            script.getSilot_status().setText("true");
        });

        // 暂停 sai
        script.getSai_pause().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedWarn(false);
            script.getSai_status().setText("false");
        });
        // 继续silot
        script.getSai_start().addActionListener((e) -> {
            script.getAccountInfoList().get(1).setNeedWarn(true);
            script.getSai_status().setText("true");
        });
    }


    private static String setPoint(char keyChar, List<AccountInfo> accountInfoList) {
        PointInfoDTO pointInfoDTO;
        Point point = RobotHelper.getPoint();
        if (keyChar == 49) {
            String retStr = "st_silot = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(0);
            pointInfoDTO = accountInfo.getSt();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 50) {
            String retStr = "ed_silot = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(0);
            pointInfoDTO = accountInfo.getEd();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 51) {
            String retStr = "st_sai = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(1);
            pointInfoDTO = accountInfo.getSt();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        } else if (keyChar == 52) {
            String retStr = "ed_sai = [%d,%d]";
            AccountInfo accountInfo = accountInfoList.get(1);
            pointInfoDTO = accountInfo.getEd();
            pointInfoDTO.setX((int) point.getX());
            pointInfoDTO.setY((int) point.getY());
            return String.format(retStr, pointInfoDTO.getX(), pointInfoDTO.getY());
        }
        return "";
    }
}
