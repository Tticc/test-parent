package com.tester.testerswing.swing;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.swing.eventHandler.EventHandle_Main;
import com.tester.testerswing.swing.eventHandler.EventHandle_Sai;
import com.tester.testerswing.swing.eventHandler.EventHandle_Silot;
import lombok.Data;

import javax.swing.*;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2022-8-4 11:10:22
 */
@Data
public class EasyScript_UI_Main {

    private JPanel main;
    private JButton checking;
    private JButton refresh;
    private JTextField point_input;
    private JLabel refresh_time;
    private JButton silot_pause;
    private JButton sai_pause;
    private JButton silot_start;
    private JButton sai_start;
    private JLabel sai_status;
    private JLabel silot_status;
    private JLabel point_print;
    private JLabel check_status;
    private JButton open_silot;
    private JButton open_sai;
    private JButton open_run_silot;
    private JButton open_run_sai;
    private JButton onTopReplica_start;
    private JLabel red_point_print;
    private JButton colos_start;
    private JButton open_colos;
    private JButton open_run_colos;
    private JButton colos_pause;
    private JLabel colos_status;
    private JButton onTopReplica_start1;
    private JButton onTopReplica_start2;
    private JButton onTopReplica_start3;
    private JButton auto_start;
    private JButton auto_stop;
    private JLabel auto_status;
    private JButton warn_start;
    private JLabel warn_status;
    private JButton open_end_silot;
    private JButton open_end_sai;
    private JButton open_end_colos;
    private JButton all_end;
    private JButton all_start;
    private JButton silot_link;
    private JButton sai_link;
    private JButton colos_link;
    private JButton warn_end;
    private JButton all_prepare;


    Boot boot = new Boot();
    List<AccountInfo> accountInfoList = boot.getAccountInfoList();

    public EasyScript_UI_Main start(Silot_Input silot_input, Sai_Input sai_input) {
        afterInit(silot_input, sai_input);
        return this;
    }

    public void afterInit(Silot_Input silot_input, Sai_Input sai_input) {
        // 初始化公共监控事件。 启动、刷新、投屏、批量true等按钮初始化
        InitCommonEventHelper.initCommonEvent(this);

        // 用于录入点阵用，后面点阵固定，已废弃 - 2023-9-21 15:19:08
        // 初始化 silot 录入事件
        EventHandle_Silot.handle_silot(silot_input);

        // 废弃 - 2023-9-21 15:19:08
        // 初始化 sai 录入事件
        EventHandle_Sai.handle_sai(sai_input);

        // 处理公共界面的其他事件。如open，return，run
        EventHandle_Main.handle_main(this);

    }
}
