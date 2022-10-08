package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.Silot_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * silot 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Silot {

    public static String pointStr = "(%d,%d)";

    public static PointInfoDTO silot_open_p1_st = new PointInfoDTO().setX(1335).setY(1051);
    public static GaussianPointInfoDTO silot_open_p1 = new GaussianPointInfoDTO().setSt(silot_open_p1_st).setEd(silot_open_p1_st);
    public static PointInfoDTO silot_open_p2_st = new PointInfoDTO().setX(1057).setY(948);
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


    public static void handle_silot(Silot_Input script) {
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

        JTextField silot_return_p2_input = script.getSilot_return_p2_input();
        setTextField(silot_return_p2_input, silot_return_p2_st);
        JTextField silot_return_p22_input = script.getSilot_return_p22_input();
        setTextField(silot_return_p22_input, silot_return_p2_ed);

        JTextField silot_return_p3_input = script.getSilot_return_p3_input();
        setTextField(silot_return_p3_input, silot_return_p3_st);
        JTextField silot_return_p32_input = script.getSilot_return_p32_input();
        setTextField(silot_return_p32_input, silot_return_p3_ed);


        // 跑路6个点
        JTextField silot_align_p1_input = script.getSilot_align_p1_input();
        setTextField(silot_align_p1_input, silot_align_p1_st);
        JTextField silot_align_p12_input = script.getSilot_align_p12_input();
        setTextField(silot_align_p12_input, silot_align_p1_ed);

        JTextField silot_align_p2_input = script.getSilot_align_p2_input();
        setTextField(silot_align_p2_input, silot_align_p2_st);
        JTextField silot_align_p22_input = script.getSilot_align_p22_input();
        setTextField(silot_align_p22_input, silot_align_p2_ed);

        JTextField silot_align_p3_input = script.getSilot_align_p3_input();
        setTextField(silot_align_p3_input, silot_align_p3_st);
        JTextField silot_align_p32_input = script.getSilot_align_p32_input();
        setTextField(silot_align_p32_input, silot_align_p3_ed);

        // 主览2个点
        JTextField silot_main_view_input = script.getSilot_main_view_input();
        setTextField(silot_main_view_input, silot_main_view_p1_st);
        JTextField silot_main_view2_input = script.getSilot_main_view2_input();
        setTextField(silot_main_view2_input, silot_main_view_p1_ed);

    }




    public static void quick_run(){
        try {
            EventHandle_Main.openOpe(EventHandle_Silot.silot_open_p1, EventHandle_Silot.silot_open_p2);
//            EventHandle_Main.runOpe(EventHandle_Silot.silot_align_p1, EventHandle_Silot.silot_align_p2, EventHandle_Silot.silot_align_p3_dock, EventHandle_Silot.silot_main_view_p1);
            EventHandle_Main.quickRunOpe(EventHandle_Silot.silot_align_p1,
                    EventHandle_Silot.silot_align_p2,
                    EventHandle_Silot.silot_align_p3,
                    EventHandle_Silot.silot_align_p4_stop,
                    EventHandle_Silot.silot_align_p5_use,
                    EventHandle_Silot.silot_align_p6_up,
                    EventHandle_Silot.silot_align_p7_up,
                    EventHandle_Silot.silot_align_p3_dock);
            // 回收无人机
//            EventHandle_Main.returnOpe(EventHandle_Silot.silot_return_p1, EventHandle_Silot.silot_return_p2, EventHandle_Silot.silot_return_p3, EventHandle_Silot.silot_main_view_p1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
