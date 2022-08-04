package com.tester.testerswing.swing;

import com.tester.testerswing.boot.AccountInfo;
import com.tester.testerswing.boot.Boot;
import lombok.Data;

import javax.swing.*;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2022-8-4 11:10:22
 */
@Data
public class EasyScript_UI {

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
    private JLabel point_input_info;
    private JLabel point_print;
    private JLabel check_status;
    private JButton open_silot;
    private JButton open_return_silot;
    private JButton open_sai;
    private JButton open_return_sai;
    private JTextField silot_open_p1_input;
    private JLabel silot_open_p1_label;
    private JLabel silot_open_p2_label;
    private JLabel silot_return_p1_label;
    private JLabel silot_return_p2_label;
    private JTextField silot_open_p2_input;
    private JTextField silot_return_p2_input;
    private JTextField silot_return_p1_input;
    private JLabel sai_open_p1_label;
    private JLabel sai_open_p2_label;
    private JLabel sai_return_p1_label;
    private JLabel sai_return_p2_label;
    private JTextField sai_open_p1_input;
    private JTextField sai_open_p2_input;
    private JTextField sai_return_p1_input;
    private JTextField sai_return_p2_input;


    Boot boot = new Boot();
    List<AccountInfo> accountInfoList = boot.getAccountInfoList();

    public EasyScript_UI start() {
        afterInit();
        return this;
    }

    public void afterInit() {
        // 初始化公共事件
        InitCommonEventHelper.initCommonEvent(this);

        // 初始化 silot 处理事件
        EventHandle_Silot.handle_silot(this);

        // 初始化 sai 处理事件
        EventHandle_Sai.handle_sai(this);

    }
}
