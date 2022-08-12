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
    public static PointInfoDTO silot_open_p2_st = new PointInfoDTO().setX(1197).setY(953);
    public static GaussianPointInfoDTO silot_open_p2 = new GaussianPointInfoDTO().setSt(silot_open_p2_st).setEd(silot_open_p2_st);

    public static PointInfoDTO silot_return_p1_st = new PointInfoDTO().setX(1706).setY(744);
    public static PointInfoDTO silot_return_p1_ed = new PointInfoDTO().setX(1778).setY(752);
    public static GaussianPointInfoDTO silot_return_p1 = new GaussianPointInfoDTO().setSt(silot_return_p1_st).setEd(silot_return_p1_ed);

    public static PointInfoDTO silot_return_p2_st = new PointInfoDTO().setX(1725).setY(845);
    public static PointInfoDTO silot_return_p2_ed = new PointInfoDTO().setX(1725).setY(845);
    public static GaussianPointInfoDTO silot_return_p2 = new GaussianPointInfoDTO().setSt(silot_return_p2_st).setEd(silot_return_p2_ed);

    public static PointInfoDTO silot_return_p3_st = new PointInfoDTO().setX(1784).setY(899);
    public static PointInfoDTO silot_return_p3_ed = new PointInfoDTO().setX(1804).setY(905);
    public static GaussianPointInfoDTO silot_return_p3 = new GaussianPointInfoDTO().setSt(silot_return_p3_st).setEd(silot_return_p3_ed);

    public static PointInfoDTO silot_align_p1_st = new PointInfoDTO().setX(1872).setY(207);
    public static PointInfoDTO silot_align_p1_ed = new PointInfoDTO().setX(1891).setY(220);
    public static GaussianPointInfoDTO silot_align_p1 = new GaussianPointInfoDTO().setSt(silot_align_p1_st).setEd(silot_align_p1_ed);

    public static PointInfoDTO silot_align_p2_st = new PointInfoDTO().setX(1647).setY(253);
    public static PointInfoDTO silot_align_p2_ed = new PointInfoDTO().setX(1672).setY(264);
    public static GaussianPointInfoDTO silot_align_p2 = new GaussianPointInfoDTO().setSt(silot_align_p2_st).setEd(silot_align_p2_ed);

    public static PointInfoDTO silot_align_p3_st = new PointInfoDTO().setX(1623).setY(102);
    public static PointInfoDTO silot_align_p3_ed = new PointInfoDTO().setX(1635).setY(114);
    public static GaussianPointInfoDTO silot_align_p3 = new GaussianPointInfoDTO().setSt(silot_align_p3_st).setEd(silot_align_p3_ed);


    public static PointInfoDTO silot_main_view_p1_st = new PointInfoDTO().setX(1660).setY(209);
    public static PointInfoDTO silot_main_view_p1_ed = new PointInfoDTO().setX(1688).setY(219);
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
