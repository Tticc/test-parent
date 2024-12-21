package com.tester.testerswing.swing;

import com.tester.testercommon.util.DateUtil;
import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.onTopReplica.StartOnTopReplica;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.eventHandler.PointHelper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }
}
