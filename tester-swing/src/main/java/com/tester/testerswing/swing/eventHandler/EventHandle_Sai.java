package com.tester.testerswing.swing.eventHandler;

import com.tester.testerswing.capture.GaussianPointInfoDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import com.tester.testerswing.robot.RobotHelper;
import com.tester.testerswing.swing.Sai_Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * sai 初始化
 *
 * @Author 温昌营
 * @Date 2022-8-4 13:47:36
 */
public class EventHandle_Sai {

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


    public static void quick_run(){
        try {
            EventHandle_Main.openOpe(EventHandle_Sai.sai_open_p1, EventHandle_Sai.sai_open_p2);
//            EventHandle_Main.runOpe(EventHandle_Sai.sai_align_p1, EventHandle_Sai.sai_align_p2, EventHandle_Sai.sai_align_p3_quick, EventHandle_Sai.sai_main_view_p1);
            EventHandle_Main.quickRunOpe(EventHandle_Sai.sai_align_p1,
                    EventHandle_Sai.sai_align_p2,
                    EventHandle_Sai.sai_align_p3,
                    EventHandle_Silot.silot_align_p4_stop,
                    EventHandle_Silot.silot_align_p5_use,
                    EventHandle_Silot.silot_align_p6_up,
                    EventHandle_Silot.silot_align_p7_up,
                    EventHandle_Sai.sai_align_p3_quick);

            // 回收无人机
//            EventHandle_Main.returnOpe(EventHandle_Sai.sai_return_p1, EventHandle_Sai.sai_return_p2, EventHandle_Sai.sai_return_p3, EventHandle_Sai.sai_main_view_p1);
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
