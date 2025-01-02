package com.tester.testerswing.swing;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import com.tester.testerswing.swing.eventHandler.EventHandle_Main;
import com.tester.testerswing.swing.eventHandler.EventHandle_Sai;
import com.tester.testerswing.swing.eventHandler.EventHandle_Silot;
import lombok.Data;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2022-8-4 11:10:22
 */
@Data
public class EasyScript_UI_Main {

    private JPanel main;
    private JButton refresh;
    private JTextField point_input;//失效
    private JLabel refresh_time;
    private JButton silot_pause;
    private JButton sai_pause;
    private JButton silot_start;
    private JButton sai_start;
    private JLabel sai_status;
    private JLabel silot_status;
    private JLabel point_print;
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
    private JButton all_harvest;
    private JButton harvest_silot;
    private JButton harvest_sai;
    private JButton harvest_colos;
    private JButton onTopReplica_start4;
    private JButton four_pause;
    private JButton four_start;
    private JLabel four_status;
    private JButton open_four;
    private JButton open_run_four;
    private JButton four_link;
    private JButton open_end_four;
    private JButton harvest_four;
    private JButton pure_harvest_silot;
    private JButton pure_harvest_sai;
    private JButton pure_harvest_colos;
    private JButton pure_harvest_four;
    private JButton pure_all_harvest;
    private JButton onTopReplica_start5;
    private JButton onTopReplica_start6;
    private JButton five_pause;
    private JButton five_start;
    private JButton six_start;
    private JButton six_pause;
    private JLabel five_status;
    private JLabel six_status;
    private JButton open_five;
    private JButton open_six;
    private JButton open_run_five;
    private JButton open_run_six;
    private JButton five_link;
    private JButton six_link;
    private JButton open_end_five;
    private JButton open_end_six;
    private JButton pure_harvest_five;
    private JButton pure_harvest_six;
    private JButton harvest_five;
    private JButton harvest_six;
    private JButton open_all;
    private JButton clean_client;


    Boot boot = new Boot();
    List<AccountInfo> accountInfoList = boot.getAccountInfoList();
    Map<Integer, AccountInfo> serialNoAccountInfoMap = boot.getSerialNoAccountInfoMap();


    public EasyScript_UI_Main start(Silot_Input silot_input, Sai_Input sai_input) {
        afterInit(silot_input, sai_input);
        return this;
    }

    public void afterInit(Silot_Input silot_input, Sai_Input sai_input) {
        // 启动本地监控
        InitCommonEventHelper.initCommonEvent(this);

        // 用于录入点阵用，后面点阵固定，已废弃 - 2023-9-21 15:19:08
        // 初始化 silot 录入事件
        EventHandle_Silot.handle_silot(silot_input, accountInfoList);

        // 废弃 - 2023-9-21 15:19:08
        // 初始化 sai 录入事件
        EventHandle_Sai.handle_sai(sai_input);

        // 处理公共界面的其他事件。如open，return，run
        EventHandle_Main.handle_main(this);

    }
}
